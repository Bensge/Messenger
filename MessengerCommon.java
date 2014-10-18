package Common;

public class MessengerCommon
{
	  public static final int OBJECT_SHELL_SIZE   = 8;
	  public static final int OBJREF_SIZE         = 4;
	  public static final int LONG_FIELD_SIZE     = 8;
	  public static final int INT_FIELD_SIZE      = 4;
	  public static final int SHORT_FIELD_SIZE    = 2;
	  public static final int CHAR_FIELD_SIZE     = 2;
	  public static final int BYTE_FIELD_SIZE     = 1;
	  public static final int BOOLEAN_FIELD_SIZE  = 1;
	  public static final int DOUBLE_FIELD_SIZE   = 8;
	  public static final int FLOAT_FIELD_SIZE    = 4;
	  
	  public static final int SCHOOL_PORT		  = 80;
	  public static final int DEFAULT_PORT		  = 1044;
	  
	  public static final int prePacketSize = 2 * INT_FIELD_SIZE;
	  
	  public static final int MESSAGE_PACKET_ID = 7;
	  
	  /*HELPER*/
	  public static int intFromBuffer(byte[] buffer, int offset)
	  {
		  return (buffer[offset + 0] & 0xFF) << 0 | (buffer[offset + 1] & 0xFF) << 8 | (buffer[offset + 2] & 0xFF) << 16 | (buffer[offset + 3] & 0xFF) << 24;
	  }
	  
	  public static byte[] bufferFromInt(int i)
	  {
		  byte[] buf = new byte[4];
		  writeIntToBuffer(i, buf, 0);
		  return buf;
	  }
	  
	  public static void writeIntToBuffer(int i, byte[] buffer, int offset)
	  {
		  buffer[offset + 0] = (byte) (i >> 0);
		  buffer[offset + 1] = (byte) (i >> 8);
		  buffer[offset + 2] = (byte) (i >> 16);
		  buffer[offset + 3] = (byte) (i >> 24);
	  }
	  
	  public static void writeBytesToBuffer(byte[] bytes, byte[] buffer, int offset)
	  {
		  for (int i = 0; i < bytes.length; i++)
		  {
			  buffer[offset + i] = bytes[i];
		  }
	  }
	  
	  public static byte[] createPrePacket(int type, int sendLength)
	  {
		  byte[] pre = new byte[prePacketSize];
			    
		  writeIntToBuffer(type, pre, 0);
		  writeIntToBuffer(sendLength, pre, INT_FIELD_SIZE);
		  return pre;
	  }
	  
	  public static byte[] mergeBuffers(byte[] b1, byte[] b2)
	  {
		  byte[] buf = new byte[b1.length + b2.length];
		  //Add b1
		  for (int i = 0; i < b1.length; i ++)
		  {
			  buf[i] = b1[i];
		  }
		  //Addb2
		  for (int i = 0; i < b2.length; i ++)
		  {
			  buf[b1.length + i] = b2[i];
		  }
		  
		  return buf;
	  }
	  
	  public static int currentUnixTime()
	  {
		  return (int)(System.currentTimeMillis() / 1000L);
	  }
	  
}


















