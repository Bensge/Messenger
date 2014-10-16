import java.io.BufferedOutputStream;

import javax.swing.SwingWorker;


public class ClientWritingWorker extends SwingWorker<Void, Void> {

	private BufferedOutputStream out;
	private String message;
	
	public ClientWritingWorker(BufferedOutputStream out, String message) {
		this.out = out;
		this.message = message;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		try {
			byte[] messagePacket = message.getBytes();
			byte[] prePacket = MessengerCommon.createPrePacket(7, messagePacket.length);
			
			out.write(prePacket);
			out.write(messagePacket);
			System.out.println("Sent message to client: " + message);
		} catch (Exception e)
		{
			System.out.println("Error sending message to client:" + e.toString() + " me: " + this);
		}
		return null;
	}
}
