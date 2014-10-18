package ClientConnection;

import Common.ChatPacket;


public class Client {
	
	private ClientReadingWorker reader;
	private ClientWritingWorker writer;
	public String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public Client(ClientReadingWorker reader){
		this.reader = reader;
	}
	
	public ClientReadingWorker getReader() {
		return reader;
	}
	
	public ClientWritingWorker getWriter() {
		return writer;
	}

	public void setWriter(ClientWritingWorker clientWritingWorker) {
		writer = clientWritingWorker;
	}
	
	public void setReader(ClientReadingWorker clientReadingWorker) {
		reader = clientReadingWorker;
	}
	
	public void send(ChatPacket packet)
	{ 
		setWriter(new ClientWritingWorker(getReader().out, packet));
		getWriter().execute();
		System.out.println("Sending message soon...");
	}
	
}
