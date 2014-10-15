import java.util.Scanner;


public class Start {
  
  
  @SuppressWarnings("unused")
public static void main(String[] args) {
    System.out.print("Enter the IP you want to connect to (or l for localhost):");
    //"192.168.178.22"
    
    int port = 80;
    String address;
    
    if (System.getProperty("os.name").startsWith("Mac"))
	{
    	port = 1044;
	}
    
    if (false)
    {
    	address = "10.32.111.6";
    }
    else
    {
    	Scanner scanner = new Scanner(System.in);
    	address = scanner.next();
    	if (address.equals("l"))
    	{
    		//Equal to localhost.
    		address = "127.0.0.1";
    	}
    }
    
    new ChatSocket(address, port);
  }
}
