package Common;

public class MessageSendPacket extends ChatPacket {
	public static int packetID = 2;
	
	public String text;
	
	
	
	public byte[] generateDataPacket()
	{
		byte[] data = text.getBytes();
		this.length = data.length;
		return data;
	}
}
