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


public class ChatSocket{
  
  //"192.168.178.22"
  private String name;
  private GUI gui;
  private Socket socket;
  private BufferedInputStream in;
  private SocketAddress address;
  private Login login;
  private boolean connected = false;
  
  public ChatSocket(String addr, int port, Login login){
    this.login = login;
    address = new InetSocketAddress(addr, port);
    socket = new Socket();
    
    if(!connect())
    	return; 
    
    System.out.println("Connected to:" + socket.getInetAddress() + " on port " + socket.getPort());
    
    gui = new GUI();
    
    gui.setSendButtonListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0)
      {
        sendText(gui.getText());
      }
    });
    
    sendName(name);
    //Let's go a different route here.
    //run();  
    ServerReadingWorker reader = new ServerReadingWorker(in, this);
    reader.execute();
  }
  
  public boolean connect(){
	//returns true if connection was succesfull
    try {
      socket.connect(address);
      connected = socket.isConnected();
      
      if(connected)
    	  login.dispose();
     
      in = new BufferedInputStream(socket.getInputStream() );
      
      //if(gui.username.equals(""))
    	//  gui.username = "unknown";
      name = Login.getUserName();
      
      return true;
     
    } catch (IOException e) {
      login.complain();
      return false;
      //e.printStackTrace();
    }
  }
  
  private void sendName(String name) {
	MessageLoginPacket packet = new MessageLoginPacket();
	packet.name = name;
	
	System.out.println(name);
	byte[] data = packet.generateDataPacket();
	byte[] pre = packet.generatePrePacket();
	  
     try {
   	  socket.getOutputStream().write(pre);
   	  socket.getOutputStream().write(data);
     }
     catch(Exception e){
    	 System.out.println("fischsalat");
   	  System.out.println("Error sxhending message: " + e.toString());
     }
     System.out.println("sent: " + name);
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
	  else if(packet instanceof MessageLoginPacket){
		  
		  MessageLoginPacket p = (MessageLoginPacket) packet;
		  
		  String date = new SimpleDateFormat("HH:mm").format(new Date());
		  String name = p.name;

		  gui.addEntry(date, "Server", name + " joined");
		  
	  }
  }
}














