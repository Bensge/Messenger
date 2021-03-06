package Server;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import ClientConnection.Client;
import ClientConnection.ClientReadingWorker;



public class SocketAcceptWorker extends SwingWorker<Void, ClientReadingWorker> {

	private ServerSocket socket;
	private ChatServer server;
	
	public SocketAcceptWorker(ServerSocket socket, ChatServer server) {
		this.socket = socket;
		this.server = server;
	}
	


	@Override
	protected Void doInBackground() throws Exception {
		System.out.println("Hi from SocketAcceptWriter!");
		while (true)
		{
			try
			{
				Socket client = socket.accept();
				if (client != null)
				{
					InputStream in = client.getInputStream();
					OutputStream out = client.getOutputStream();
					
					ClientReadingWorker worker = new ClientReadingWorker(in, out, server);
					
					worker.execute();
					
					publish(worker);
				}
			}
			catch (Exception e)
			{
				System.out.println("Error accepting connection: " + e.toString());
			}
			
		}
		
	}
	
	@Override
	protected void process(List<ClientReadingWorker> chunks) {
		for (ClientReadingWorker reader : chunks)
		{
			Client client = new Client(reader);
			reader.client = client;
			server.registerClient(client);
		}
	}
	
	@Override
	protected void done() {
		System.out.println("SocketAcceptWorker: exiting!");
	}

}
