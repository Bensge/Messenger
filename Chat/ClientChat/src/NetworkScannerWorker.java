import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.swing.SwingWorker;


public class NetworkScannerWorker extends SwingWorker<Void, ServiceEvent>{

	private int eventType;
	private Login login;
	private ServiceEvent e;
	
	public NetworkScannerWorker(Login login) {
		this.login = login;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		 System.out.println("Setting up DNS");
		    JmDNS dns = null;
			try {
				dns = JmDNS.create();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		    dns.addServiceListener("_messenger._tcp.local.", new ServiceListener(){
		    	@Override
		    	public void serviceAdded(ServiceEvent event){
		    		System.out.println("Service added: " + event.toString());
		    		eventType = 0;
		    		e = event;
		    	}

				@Override
				public void serviceRemoved(ServiceEvent event) {
					// TODO Auto-generated method stub
					System.out.println("Service removed: " + event.toString());
					eventType = 1;
					e = event;
				}

				@Override
				public void serviceResolved(ServiceEvent event) {
					// TODO Auto-generated method stub
					System.out.println("Service resolved: " + event.toString());
					eventType = 2;
					e = event;
				}
		    });
		    
		    
		    publish(e);
		    
		    return null;
	}

	
	@Override
	protected void process(List<ServiceEvent> chunks) {
		super.process(chunks);
		
		for(ServiceEvent chunk : chunks){
			if(eventType == 1){
				login.serviceRemoved(chunk);
			}else if(eventType == 2){
				login.serviceResolved(chunk);
			}
		}
	}
}
