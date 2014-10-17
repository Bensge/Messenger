package ClientConnection;


public class Client {
	
	private ClientReadingWorker reader;
	private ClientWritingWorker writer;
	
	
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
	
}
