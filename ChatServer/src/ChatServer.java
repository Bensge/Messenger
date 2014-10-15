import java.net.*;
import java.io.*;

public class ChatServer {
  
  
  // java.lang.Object shell size in bytes:
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
  
  
  public static final int MESSAGE_PACKET_ID = 0;
  
  
  public static void main(String[] args)
  {
    ChatServer s = new ChatServer();
  }
  
  /*NOT SO CONSTANT CONSTANTS*/
  private int port = 80;
  
  /*IVARS*/
  private ServerSocket socket;
  private Socket clientSocket = null;
  private BufferedInputStream in;
  
  /*HELPER*/
  protected static int intFromBuffer(byte[] buffer, int offset)
  {
	  return (buffer[offset + 0] & 0xFF) << 0 | (buffer[offset + 1] & 0xFF) << 8 | (buffer[offset + 2] & 0xFF) << 16 | (buffer[offset + 3] & 0xFF) << 24;
  }
  
  protected static byte[] bufferFromInt(int i)
  {
	  byte[] buf = new byte[4];
	  writeIntToBuffer(i, buf, 0);
	  return buf;
  }
  
  protected static void writeIntToBuffer(int i, byte[] buffer, int offset)
  {
	  buffer[offset + 0] = (byte) (i >> 0);
	  buffer[offset + 1] = (byte) (i >> 8);
	  buffer[offset + 2] = (byte) (i >> 16);
	  buffer[offset + 3] = (byte) (i >> 24);
  }
  
  /*METHODS*/
  public ChatServer()
  {
    //Hello
	if (System.getProperty("os.name").startsWith("Mac"))
	{
		port = 1044;
	}
    System.out.println("ChatServer by Justus & Benno");
    System.out.println("+----------------------------+");
    System.out.println("|           PREMIUM          |");
    System.out.println("+----------------------------+\n");
    
    while (true) {
      startUp();
      listen();
      tearDown(); 
    }
  }
  
  private void startUp()
  {
    //Open socket
    try { 
      socket = new ServerSocket(port);
    } catch(Exception e) {
      System.out.println("Error opening socket: " + e.toString());
    }
    try { 
      //Get inet address
      System.out.println("Server address: " + socket.getInetAddress()+  " Server IP: " + InetAddress.getLocalHost().getHostAddress());    
    } catch(Exception e) {
      System.out.println("Error getting local IP address: " + e.toString());
    }
    //Accept connections
    try { 
      clientSocket = socket.accept();
    } catch(Exception e) {
      System.out.println("Error accepting from socket: " + e.toString());
    }
    
    //Check socket
    if (clientSocket != null) {
      System.out.println("Client connected: " + clientSocket.toString());
    } // end of if
    
  }
  
  private void tearDown()
  {
    //Close socket
    try { 
      socket.close();
    } catch(Exception e) {
      
    } finally {
      
    } // end of try
  }
  
  private void listen()
  {
    try { 
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedInputStream(clientSocket.getInputStream() );
      
      byte[] prePacket = new byte[INT_FIELD_SIZE * 2];
      
      System.out.println("Listening to client messages!");
      
      System.out.println("Now ready!");
      while (true)
      {
        in.read(prePacket,0,8);
        System.out.println("Received Pre-Packet!" + prePacket);
        
        int packetType = intFromBuffer(prePacket, 0);
        System.out.println("Packet type: " + packetType);
        
        int packetSize = intFromBuffer(prePacket, 4);
        System.out.println("Packet size: " + packetSize);
        
        
        byte[] messageBuffer = new byte[packetSize];
        in.read(messageBuffer,0,packetSize);
        
        String message = new String(messageBuffer);
        System.out.println("Message: " + message);
        
        
      } //end of for loop
    } catch(Exception e){
      System.out.println("Error while reading client socket " + e.toString());
      try {
        in.close();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }
}