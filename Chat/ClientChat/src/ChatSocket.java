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
				send(gui.getText());
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
	
	public void send(String msg){
		
		System.out.println("sent: " + msg);
		try {
			socket.getOutputStream().write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
