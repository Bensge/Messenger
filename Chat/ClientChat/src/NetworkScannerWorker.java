import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.print.CancelablePrintJob;
import javax.swing.SwingWorker;


public class NetworkScannerWorker extends SwingWorker<Void, Map<String, Object>>{

	private Login login;
	private JmDNS dns = null;
	private ServiceListener listener;

	public NetworkScannerWorker(Login login) {
		this.login = login;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		 System.out.println("Setting up DNS");
			try {
				dns = JmDNS.create();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		    dns.addServiceListener("_messenger._tcp.local.", listener = new ServiceListener(){
		    	@Override
		    	public void serviceAdded(ServiceEvent event){
		    		System.out.println("Service added: " + event.toString());
		    		Map<String, Object> dictionary = new HashMap<String, Object>();
		    		dictionary.put("event", event);
		    		dictionary.put("type", new Integer(0));
		    		publish(dictionary);
		    	}

				@Override
				public void serviceRemoved(ServiceEvent event) {
					System.out.println("Service removed: " + event.toString());
					Map<String, Object> dictionary = new HashMap<String, Object>();
		    		dictionary.put("event", event);
		    		dictionary.put("type", new Integer(1));
		    		publish(dictionary);
				}

				@Override
				public void serviceResolved(ServiceEvent event) {
					System.out.println("Service resolved: " + event.toString());
					Map<String, Object> dictionary = new HashMap<String, Object>();
		    		dictionary.put("event", event);
		    		dictionary.put("type", new Integer(2));
		    		publish(dictionary);
				}
		    });
		    
		    return null;
	}
	
	public void stop() {
		
		Runnable r = new Runnable() {
	         public void run() {
	        	dns.removeServiceListener("_messenger._tcp.local.", listener);
	     		try {
	     			dns.close();
	     			System.out.println("Shut down DNS");
	     		} catch (IOException e) {
	     			e.printStackTrace();
	     		}
	         }
	     };

	     ExecutorService executor = Executors.newCachedThreadPool();
	     executor.submit(r);
	     executor.shutdown();
	}

	
	@Override
	protected void process(List<Map<String, Object>> chunks) {
		super.process(chunks);
		
		for(Map<String,Object> event : chunks)
		{
			int eventType = ((Integer)event.get("type")).intValue();
			ServiceEvent serviceEvent = (ServiceEvent)event.get("event");
					
			if (eventType == 1)
			{
				login.serviceRemoved(serviceEvent);
			}
			else if(eventType == 2)
			{
				login.serviceResolved(serviceEvent);
			}
		}
	}
}









