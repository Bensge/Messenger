import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
  private PrintWriter writer;
  private BufferedReader in;
  private Scanner s;
  private SocketAddress address;
  private boolean connected = false;
  private char[] input = new char[1];
  
  public ChatSocket(String addr, int port){
    
    s = new Scanner(System.in);
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
    
    try {
      //writer = new PrintWriter(socket.getOutputStream());
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    run();  
  }
  
  public void start(){
    try {
      socket.connect(address);
      connected = socket.isConnected();
    } catch (IOException e) {
      
      e.printStackTrace();
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
    String ret = "";
    
    try {
      for(in.read(input, 0, 1); true; in.read(input, 0, 1)){
        ret += input[0];
        return ret;
      }
      
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
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
  
}
