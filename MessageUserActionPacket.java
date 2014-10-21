package Common;

public class MessageUserActionPacket extends ChatPacket {
	
	public static int packetID = 6;
	
	public enum Action {
		Join,
		Leave
	};
	
	public String user;
	public Action action;
	public boolean isCurrent;
	
	public MessageUserActionPacket()
	{
		
	}
	
	public MessageUserActionPacket(String user, Action action, boolean isCurrent)
	{
		this.user = user;
		this.action = action;
		this.isCurrent = isCurrent;
	}
	
	public byte[] generateDataPacket()
	{
		int act = this.action.ordinal();
		if (act > Byte.MAX_VALUE)
		{
			System.out.println("This is a fatal error! Enum Action value is bigger than the available byte!!!");
		}
		byte ac = (byte)act;
		byte isC = (byte)(this.isCurrent ? 1 : 0);
		
		byte[] usr = user.getBytes();
		int userLength = usr.length;
		
		int totalLength = 2 + 4 + userLength;
		byte[] packet = new byte[totalLength];
		this.length = totalLength;
		
		packet[0] = ac;
		packet[1] = isC;
		MessengerCommon.writeIntToBuffer(userLength, packet, 2);
		MessengerCommon.writeBytesToBuffer(usr, packet, 2+4);
		
		return packet;
	}
	
	public String toString()
	{
		return this.getClass().toString() + " (" + this.user + " did action:" + this.action + " isCurrent: " + this.isCurrent + " )";
	}
}