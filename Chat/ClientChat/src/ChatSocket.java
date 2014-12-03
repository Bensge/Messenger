
import java.awt.event.ActionEvent;

import javax.jmdns.*;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Common.*;
import Common.MessageUserActionPacket.Action;


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
    	@Override
    	public void actionPerformed(ActionEvent arg0)
    	{
    		sendText(gui.getText());
    	}
    });
    
    gui.setDataListener(new DataSendListener() {
		@Override
		public void sendObject(Object o, boolean isImage) {
			
				MessageFilePacket p;
		
				if(isImage)
					p = new MessageImagePacket();
				else
					p = new MessageFilePacket();
			
				p.file = (File) o;
				System.out.println("loglog");
				byte[] data = p.generateDataPacket();
				System.out.println((data==null) + "data == null");
			 	byte[] pre = p.generatePrePacket();
			 	System.out.println((pre == null) + "pre == null");
			 	try {
			 		socket.getOutputStream().write(pre);
			 		socket.getOutputStream().write(data);
			 		socket.getOutputStream().flush();
			 	}
			 	catch(Exception e){
			 		System.out.println("Error sending file: " + e.toString());
			 	}
			 	System.out.println("sent file");
			//}
		}

	});
    
   
    
    
    sendName(name);
    //Let's go a different route here.
    //run();  
    ServerReadingWorker reader = new ServerReadingWorker(in, this);
    reader.execute();
    
  }
  
  public boolean connect(){
	//returns true if connection was successful
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
	  else if (packet instanceof MessageImagePacket)
	  {
		  String date = "Not now";
		  date = new SimpleDateFormat("HH:mm").format(new Date());
		  
		  gui.addEntry(date, "DUNNO", ((MessageImagePacket)packet).getBufferedImage());
	  }
	  else if (packet instanceof MessageFilePacket){
		  String date = new SimpleDateFormat("HH:mm").format(new Date());
		  gui.addEntry(date, "Mysterio", "You just got filed");
	  }
	  else if(packet instanceof MessageLoginPacket)
	  {
		  System.out.println("This should not be called anymore!!! Deprecated!!! ");
		  
		  MessageLoginPacket p = (MessageLoginPacket) packet;
		  
		  String date = new SimpleDateFormat("HH:mm").format(new Date());
		  String name = p.name;

		  gui.addEntry(date, "Server", name + " joined");
	  }
	  else if (packet instanceof MessageUserActionPacket)
	  {
		  MessageUserActionPacket p = (MessageUserActionPacket)packet;
		  System.out.println("Packet: " + packet.toString());
		  
		  
		  gui.noteUserAction(p.action, p.user);
		  //Only show server message if the action is relevant and just happened.
		  if (p.isCurrent)
		  {
			  String date = new SimpleDateFormat("HH:mm").format(new Date());
			  String sender = "Server";
			  String text = p.user + " " + (p.action == Action.Join ? "joined" : "left");
			  
			  gui.addEntry(date, sender, text);
		  }
	  }
  }
}














