import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;


public class ChatSocket implements Runnable{
  
  //"192.168.178.22"
  private GUI gui;
  private final int prePacketSize = 8;
  private Socket socket;
  private Thread receive;
  private BufferedInputStream in;
  private SocketAddress address;
  private PrintWriter out;
  private boolean connected = false;
  private char[] input = new char[1];
  
  public static final int INT_FIELD_SIZE = 4;
  
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
      out = new PrintWriter(socket.getOutputStream(), true);
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
    int[] prePacket = new int[prePacketSize];
    
    //art des gesendeten
    prePacket[0] = 7;       
    //l�nge des gesendeten
    prePacket[1] = toSend.length;  
    //prePacket[1] = 1000000;
    
    byte[] pre = new byte[8];
    for (int i = 0; i< 2; i++) {
      int b = prePacket[i];
      int pos = i * 4;
      pre[pos] = (byte) (b);
      pre[pos + 1] = (byte) (b >> 8);
      pre[pos + 2] = (byte) (b >> 16);
      pre[pos + 3] = (byte) (b >> 24);
    } // end of for
    
    byte[] res = new byte[toSend.length + prePacketSize];
    
    for(int b = 0; b < res.length; b++){
    	res[b] = b < prePacketSize ? pre[b] : toSend[b - prePacketSize];
    }
    
    try{
      socket.getOutputStream().write(pre);
      socket.getOutputStream().write(toSend);
      
    }
    catch(Exception e){}
    System.out.println("sent: " + msg);
  }
  
  public String receive(){
    
    try { 
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedInputStream(socket.getInputStream() );
        
        byte[] prePacket = new byte[INT_FIELD_SIZE * 2];
        
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
