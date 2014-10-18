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
	  MessageSendPacket packet = new MessageSendPacket();
	  packet.text = msg;
	  byte[] data = packet.generateDataPacket();
	  byte[] pre = packet.generatePrePacket();
	  
      try {
    	  socket.getOutputStream().write(pre);
    	  socket.getOutputStream().write(data);
      }
      catch(Exception e){
    	  System.out.println("Error sending message: " + e.toString());
      }
      System.out.println("sent: " + msg);
  }
    
  public void processMessage(ChatPacket packet)
  {
	  System.out.println("Received packet of class: " + packet.getClass().toString());
	  if (packet instanceof MessageReceivePacket)
	  {
		  MessageReceivePacket p = (MessageReceivePacket) packet;
		  
		  String date = new SimpleDateFormat("HH:mm").format(new Date((long)p.timestamp * 1000));
		  String sender = p.sender;
		  String text = p.text;
		  
		  gui.addEntry(date, sender, text);
	  }
  }
}














