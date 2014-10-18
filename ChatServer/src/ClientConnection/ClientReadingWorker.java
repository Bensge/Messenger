package ClientConnection;

import Common.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingWorker;

import Server.ChatServer;


public class ClientReadingWorker extends SwingWorker<Void, ChatPacket> {

	private InputStream in;
	public OutputStream out;
	private ChatServer server;
	public Client client;
	
	public ClientReadingWorker(InputStream in, OutputStream out, ChatServer server) {
		this.in = in;
		this.out = out;
		this.server = server;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		byte[] prePacket = new byte[MessengerCommon.INT_FIELD_SIZE * 2];
		
	    System.out.println("Listening to client messages!");
	    System.out.println("Now ready!");
	    while (true)
	    {
	    	//Clean out prePacket for the case that it's filled with the old package.
	    	Arrays.fill( prePacket, (byte) 0 );
	        int result = in.read(prePacket,0,8);
	        
	        if (result == -1)
	        {
	        	System.out.println("Client disconnected!");
	        	//It's been great talking to you, Client!
	        	break;
	        }
	        
	        int packetType = MessengerCommon.intFromBuffer(prePacket, 0);
	        
	        if (packetType == 0)
	        {
	        	//Got invalid packet
	        	continue;
	        }
	        
	        System.out.println("Received Pre-Packet!" + prePacket);
	        
	        System.out.println("Packet type: " + packetType);
	        
	        int packetSize = MessengerCommon.intFromBuffer(prePacket, 4);
	        System.out.println("Packet size: " + packetSize);
	        
	        byte[] packetBuffer = new byte[packetSize];
	        in.read(packetBuffer,0,packetSize);
	        
	        ChatPacket packet = ChatPacket.parseDataPacket(prePacket, packetBuffer);
	        
	        publish(packet);
	    }
	    
	    return null;
	}
	
	@Override
	protected void process(List<ChatPacket> chunks) {
		// TODO Auto-generated method stub
		for (ChatPacket p : chunks)
		{
			server.processMessage(this.client, p);
		}
	}
	
	@Override
	protected void done() {
		server.unregisterClient(this.client);
	}

}
