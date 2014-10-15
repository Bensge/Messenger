import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class Start {
 
	public static String USERNAME;
	private static JOptionPane pane;
  
  @SuppressWarnings("unused")
public static void main(String[] args) {
    //System.out.print("Enter the IP you want to connect to (or l for localhost):");
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
    	//Scanner scanner = new Scanner(System.in);
    	//address = scanner.next();
    	/*address = JOptionPane.showInputDialog("Enter the IP you want to connect to (or <enter> for localhost):");
    	if (address.equals(""))
    	{
    		//Equal to localhost.
    		address = "127.0.0.1";
    	}
    	*/
    	//>>>>>
    	//Equal to localhost.
		address = "127.0.0.1";
    	JTextField username = new JTextField();
    	JTextField addressField = new JTextField();
    	Object[] message = {
    	    "Username:", username,
    	    "Enter the IP you want to connect to (or <enter> for localhost):", addressField
    	};
	
    	int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.DEFAULT_OPTION);
    	if (option == JOptionPane.OK_OPTION) {
    		USERNAME = username.getText();
    		
    		if(username.getText() != "" && addressField.getText() != "")
    			 new ChatSocket(address, port);
    		else if(addressField.getText() != "" && username.getText() != ""){
    			new ChatSocket(addressField.getText(), port);
    		}
    	}
    	else{
    		System.out.println("Login failed");
    	}
    }
    
   
  }
}
