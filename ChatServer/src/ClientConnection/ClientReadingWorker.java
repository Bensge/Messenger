package ClientConnection;
import Server.MessengerCommon;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.util.List;

import javax.swing.SwingWorker;

import Server.ChatServer;


public class ClientReadingWorker extends SwingWorker<Void, String> {

	private BufferedInputStream in;
	public OutputStream out;
	private ChatServer server;
	
	public ClientReadingWorker(BufferedInputStream in, OutputStream out, ChatServer server) {
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
	        in.read(prePacket,0,8);
	        
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
	        
	        String message = new String(packetBuffer);
	        
	        publish(message);
	    }
	}
	
	@Override
	protected void process(List<String> chunks) {
		// TODO Auto-generated method stub
		for (String msg : chunks)
		{
			server.processMessage(this, msg);
		}
	}

}
