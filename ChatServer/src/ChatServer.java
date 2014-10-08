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
  
  
  
  public static void main(String[] args)
  {
    ChatServer s = new ChatServer();
  }
  
  /*CONSTANTS*/
  private final int port = 1026;
  
  /*IVARS*/
  private ServerSocket socket;
  private Socket clientSocket = null;
  private BufferedReader in;
  
  /*METHODS*/
  public ChatServer()
  {
    //Hello
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
      in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );
      
      char[] input = new char[1];
      
      System.out.println("Listening to client messages!");
      //sizeof()
      while(!in.ready())
      {
        
      }
      System.out.println("Now ready!");
      while (true)
      {
        in.read(input,0,1);
        System.out.println("received: " + input[0]);
        
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