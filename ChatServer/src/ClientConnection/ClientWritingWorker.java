package ClientConnection;
import Common.ChatPacket;
import Common.MessengerCommon;

import java.io.OutputStream;

import javax.swing.SwingWorker;


public class ClientWritingWorker extends SwingWorker<Void, Void> {

	private OutputStream out;
	private ChatPacket packet;
	
	public ClientWritingWorker(OutputStream out, ChatPacket p) {
		this.out = out;
		this.packet = p;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		try {
			byte[] messagePacket = packet.generateDataPacket();
			byte[] prePacket = packet.generatePrePacket();
			System.out.println(messagePacket==null);
			out.write(prePacket);
			out.write(messagePacket);
			System.out.println("Sent message to client");
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Error sending message to client:" + e.toString() + " me: " + this);
		}
		return null;
	}
}
