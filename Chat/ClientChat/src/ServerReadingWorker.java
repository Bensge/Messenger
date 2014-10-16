
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.SwingWorker;


public class ServerReadingWorker extends SwingWorker<Void, String> {

	private BufferedInputStream in;
	private ChatSocket server;
	
	public ServerReadingWorker(BufferedInputStream in, ChatSocket server) {
		this.in = in;
		this.server = server;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		byte[] prePacket = new byte[MessengerCommon.INT_FIELD_SIZE * 2];
		
	    System.out.println("Listening to server messages!");
	    System.out.println("Now ready!");
	    while (true)
	    {
	        in.read(prePacket,0,8);
	        System.out.println("Received Pre-Packet!" + prePacket);
	        
	        int packetType = MessengerCommon.intFromBuffer(prePacket, 0);
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
			server.processMessage(msg);
		}
	}

}
