import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public class ChatSocket implements Runnable{
  
  //"192.168.178.22"
  private GUI gui;
  private Socket socket;
  private Thread receive;
  private BufferedInputStream in;
  private SocketAddress address;
  private boolean connected = false;
  
  public ChatSocket(String addr, int port){
    
    address = new InetSocketAddress(addr, port);
    socket = new Socket();
    
    start(); 
    
    System.out.println("Connected to:" + socket.getInetAddress() + " on port " + socket.getPort());
    
    gui = new GUI();
    
    gui.setSendButtonListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0)
      {
        sendText(gui.getText());
      }
    });
    
    run();  
  }
  
  public void start(){
    try {
      socket.connect(address);
      connected = socket.isConnected();
     
      in = new BufferedInputStream(socket.getInputStream() );
    } catch (IOException e) {
      
      e.printStackTrace();
    }
  }
  
  @Override
  public void run() {
	 
	  receive = new Thread(new Runnable() {	
  		@Override
  		public void run() {
  			String msg;
  			while(true){
  				System.out.println("lulu");
  				String text = receive();
  				if(text != null)
  					gui.write(text);
  				System.out.println("lala");
  			}
  		}
  	});	
	  receive.start();
	  
    while(connected){
      //String f = receive();	
      
      connected = socket.isConnected();
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  public void sendText(String msg){
    //for text
    byte[] toSend = msg.getBytes();
    byte[] pre = MessengerCommon.createPrePacket(7, toSend.length);
    
    byte[] res = MessengerCommon.mergeBuffers(pre, toSend);
    
    try {
      socket.getOutputStream().write(pre);
      socket.getOutputStream().write(toSend);
    }
    catch(Exception e){
    	System.out.println("Error sending message: " + e.toString());
    }
    System.out.println("sent: " + msg);
  }
  
  public String receive(){
    
    try { 
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedInputStream(socket.getInputStream() );
        
        byte[] prePacket = new byte[MessengerCommon.INT_FIELD_SIZE * 2];
        
        System.out.println("Listening to client messages!");
        
        System.out.println("Now ready!");
        while (true)
        {
          in.read(prePacket,0,8);
          System.out.println("Received Pre-Packet!" + prePacket);
          
          int packetType = (prePacket[0] & 0xFF) << 0 | (prePacket[1] & 0xFF) << 8 | (prePacket[2] & 0xFF) << 16 | (prePacket[3] & 0xFF) << 24;
          System.out.println("Packet type: " + packetType);
          
          int packetSize = (prePacket[4+0] & 0xFF) << 0 | (prePacket[4+1] & 0xFF) << 8 | (prePacket[4+2] & 0xFF) << 16 | (prePacket[4+3] & 0xFF) << 24;
          System.out.println("Packet size: " + packetSize);
     
          byte[] messageBuffer = new byte[packetSize];
          in.read(messageBuffer,0,packetSize);
          
          String message =new String(messageBuffer);
          System.out.println("Message: " + message);
          return message;
          
        } //end of for loop
      } catch(Exception e){
        System.out.println("Error while reading client socket " + e.toString());
       
        try {
          in.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        return null;
      }
  }
}
