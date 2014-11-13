package Server;
import java.net.*;
import javax.jmdns.JmDNS;
import java.util.ArrayList;
import java.awt.EventQueue;

import ClientConnection.Client;
import ClientConnection.ClientReadingWorker;
import ClientConnection.ClientWritingWorker;
import Common.ChatPacket;
import Common.MessageFilePacket;
import Common.MessageImagePacket;
import Common.MessageLoginPacket;
import Common.MessageReceivePacket;
import Common.MessageSendPacket;
import Common.MessageUserActionPacket;
import Common.MessengerCommon;
import Common.MessageUserActionPacket.Action;

public class ChatServer {
  
  public static final int MESSAGE_PACKET_ID = 0;
  
  
  public static void main(String[] args) throws Exception
  {
	  EventQueue.invokeLater(new Runnable() {
		@Override
		public void run() {
			try {
				ChatServer s = new ChatServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	  });
	  
	  while(true)
	  {
		  //This is stupid. Stupid, stupid, stupid!
		  Thread.sleep(1000);
	  }
  }
  
  /*NOT SO CONSTANT CONSTANTS*/
  private int port = MessengerCommon.SCHOOL_PORT;
  
  /*IVARS*/
  private ServerSocket socket;
  
  private ArrayList<Client> clients = new ArrayList<Client>();
  /*HELPER*/

  
  /*METHODS*/
  public ChatServer() throws Exception
  {
    //Hello
	String os = System.getProperty("os.name");
	if (os.startsWith("Mac") || os.startsWith("Windows 8") || os.startsWith("Linux"))
	{
		port = MessengerCommon.DEFAULT_PORT;
	}
    System.out.println("ChatServer by Justus & Benno");
    System.out.println("+----------------------------+");
    System.out.println("|           PREMIUM          |");
    System.out.println("+----------------------------+\n");
    
    //while (true) {
      startUp();
      listen();
      //tearDown(); 
    //}
      
     System.out.println("ChatServer: Bye bye!");
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
    
    JmDNS dnsServer;
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
  
  private void listen() throws Exception
  {
	  SocketAcceptWorker acceptWorker = new SocketAcceptWorker(socket,this);
	  acceptWorker.execute();
  }
  
  public void registerClient(Client client)
  {
	  clients.add(client);
	  
  }
  
  public void unregisterClient(Client client)
  {
	  clients.remove(client);
	  
	  //Notify other clients
	  MessageUserActionPacket packet = new MessageUserActionPacket(client.getName(), Action.Leave, true);
	  
	  sendPacketToClientsBut(client, packet);
  }
  
  public void processMessage(Client sender, ChatPacket packet)
  {
	  System.out.println("Processing message...");
	  
	  ChatPacket newPacket = null;
	  if (packet instanceof MessageSendPacket)
	  {
		  MessageReceivePacket p = new MessageReceivePacket();
		  p.text = ((MessageSendPacket) packet).text;
		  p.timestamp = MessengerCommon.currentUnixTime();
		  p.sender = sender.getName();
		  newPacket = p;
	  }
	  else if(packet instanceof MessageLoginPacket){
		  //get name of packet
		  sender.setName(((MessageLoginPacket) packet).name);
		  
		  //Send client connect confirmation
		  MessageReceivePacket confirmationPacket = MessageReceivePacket.serverMessagePacket("You joined!");
		  sender.send(confirmationPacket);
		  
		  for (Client client : clients)
		  {
			  if (client == sender)
				  continue;
			  //Let newly joined sender know which other clients are connected
			  MessageUserActionPacket user = new MessageUserActionPacket(client.getName(), Action.Join, false);
			  sender.send(user);
		  }
		  
		  //Notify other clients
		  MessageUserActionPacket p = new MessageUserActionPacket(sender.getName(), Action.Join, true);
		  newPacket = p;
	  }
	  else if (packet instanceof MessageImagePacket)
	  {
		  newPacket = packet;
		  System.out.println("right one");
	  }
	  else if(packet instanceof MessageFilePacket){//instance of wrong cause of subclass stuff
		  newPacket = packet;
		  System.out.println("wrong one");
	  }
	  
	  if (newPacket != null)
		  sendPacketToClientsBut(sender,newPacket);
  }
  
  public void sendPacketToClientsBut(Client sender, ChatPacket newPacket)
  {
	  for (Client client : clients)
	  {
		  if (client != sender)
		  {
			 client.send(newPacket);
		  }
	  }
  }
  
  public ArrayList<Client> getClients(){
	  return clients;
  }
}