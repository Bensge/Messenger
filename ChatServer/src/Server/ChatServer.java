package Server;
import java.net.*;
import java.util.ArrayList;
import java.awt.EventQueue;

import ClientConnection.Client;
import ClientConnection.ClientReadingWorker;
import ClientConnection.ClientWritingWorker;

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
  private int port = 80;
  
  /*IVARS*/
  private ServerSocket socket;
  
  private ArrayList<Client> clients = new ArrayList<Client>();
  /*HELPER*/

  
  /*METHODS*/
  public ChatServer() throws Exception
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
	  //clients.get(clients.size()).setCWriter(clientWritingWorker);
  }
  
  public void processMessage(ClientReadingWorker sender, String message)
  {
	  System.out.println("Processing message...");
	  for (Client client : clients)
	  {
		  if (client.getCreader() != sender)
		  {
			 // ClientWritingWorker writer = new ClientWritingWorker(client.out, message);
			 client.setCWriter(new ClientWritingWorker(sender.out, message));
			 // writer.execute();
			  client.getCWriter().execute();
			  System.out.println("Sending message soon...");
		  }
	  }
  }
  
  public ArrayList<Client> getClients(){
	  return clients;
  }
}