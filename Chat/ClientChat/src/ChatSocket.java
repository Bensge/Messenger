import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import Common.*;


public class ChatSocket implements Runnable{
  
  //"192.168.178.22"
  private GUI gui;
  private Socket socket;
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
    
    
    //Let's go a different route here.
    //run();  
    ServerReadingWorker reader = new ServerReadingWorker(in, this);
    reader.execute();
  }
  
  public void start(){
    try {
      socket.connect(address);
      connected = socket.isConnected();
     
      in = new BufferedInputStream(socket.getInputStream() );
      
      //if(gui.username.equals(""))
    	//  gui.username = "unknown";
      
      //sendText(gui.username);
     
    } catch (IOException e) {
      
      e.printStackTrace();
    }
  }
  
  @Override
  public void run() {
	  
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
    byte[] pre = MessengerCommon.createPrePacket(MessengerCommon.MESSAGE_PACKET_ID, toSend.length);
    
    
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
        //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedInputStream(socket.getInputStream() );
        
        byte[] prePacket = new byte[MessengerCommon.INT_FIELD_SIZE * 2];
        
        System.out.println("Listening to client messages!");
        
        System.out.println("Now ready!");
        while (true)
        {
          in.read(prePacket,0,8);
          System.out.println("Received Pre-Packet!" + prePacket);
          
          int packetType = MessengerCommon.intFromBuffer(prePacket, 0);
          System.out.println("Packet type: " + packetType);
          
          int packetSize = MessengerCommon.intFromBuffer(prePacket, 4);
          System.out.println("Packet size: " + packetSize);
     
          byte[] messageBuffer = new byte[packetSize];
          in.read(messageBuffer,0,packetSize);
          
          String message = new String(messageBuffer);
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
  
  public void processMessage(String msg)
  {
	  String date = new SimpleDateFormat("HH:mm").format(new Date());
	  gui.addEntry(date, "Not you", msg);
  }
}
