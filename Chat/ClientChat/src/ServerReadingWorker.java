
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingWorker;
import javax.sql.CommonDataSource;

import Common.*;

public class ServerReadingWorker extends SwingWorker<Void, ChatPacket> {

	private BufferedInputStream in;
	private ChatSocket server;
	
	public ServerReadingWorker(BufferedInputStream in, ChatSocket server) {
		this.in = in;
		this.server = server;
	}
	
	@Override
	protected Void doInBackground() throws Exception
	{
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
	        
	        System.out.println("Received Pre-Packet!");
	        System.out.println("Packet type: " + packetType);
	        
	        int packetSize = MessengerCommon.intFromBuffer(prePacket, 4);
	        System.out.println("Packet size: " + packetSize);
	        
	        byte[] packetBuffer = new byte[packetSize];
	        
	        result = 0;
	        int receivedCount = 0;
	        while (receivedCount < packetSize)
	        {
		        result = in.read(packetBuffer,receivedCount,packetSize - receivedCount);
		        if (result < 0)
		        {
		        	System.out.println("Error reading big packet!!!" + result);
		        	break;
		        }
		        else
		        {
		        	receivedCount += result;
		        	if (receivedCount < packetSize)
			        {
			        	System.out.println("Reading progress: " + ((float)receivedCount / (float)packetSize * 100.f) + "%");
			        }
		        }
	        }
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
			server.processMessage(p);
		}
	}

}
