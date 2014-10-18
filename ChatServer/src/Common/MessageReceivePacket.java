package Common;

public class MessageReceivePacket extends MessageSendPacket {
	public final static int packetID = 2;
	
	public String sender;
	public int timestamp;
}
