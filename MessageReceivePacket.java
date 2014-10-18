package Common;

public class MessageReceivePacket extends MessageSendPacket {
	public static int packetID = 2;
	
	public String sender;
	public int timestamp;
	
	public byte[] generateDataPacket()
	{
		byte[] senderBytes = sender.getBytes();
		int senderLength = senderBytes.length;
		
		byte[] textBytes = text.getBytes();
		int textLength = textBytes.length;
		
		this.length = /*Timestamp*/ 4 + /*Sender-Length*/ 4 + /*sender*/ senderLength + /*text*/ textLength;
		
		byte[] data = new byte[this.length];
		
		MessengerCommon.writeIntToBuffer(timestamp, data, 0);
		MessengerCommon.writeIntToBuffer(senderLength, data, 4);
		MessengerCommon.writeBytesToBuffer(senderBytes, data, 8);
		MessengerCommon.writeBytesToBuffer(textBytes, data, 8 + senderLength);
		
		return data;
	}
}
