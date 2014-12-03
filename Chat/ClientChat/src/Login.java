import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.jmdns.ServiceEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JMenu;

import Common.MessengerCommon;

import java.awt.Font;


public class Login extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;

	private ChatSocket socket;
	
	private int port;
	private String address;
	private static String name;
	private JPanel contentPane;
	private JTextField IPTextField;
	private JTextField nameTextField;
	private JTable serverTable;
	JButton OK_Button;
	JButton Cancel_Button;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
			
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		
		 port = MessengerCommon.SCHOOL_PORT; 
		    
		 if (System.getProperty("os.name").startsWith("Mac") || System.getProperty("os.name").startsWith("Windows 8"))
		{
		   	port = MessengerCommon.DEFAULT_PORT;
		}
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(697, 517);
		setLocationRelativeTo(null);
		setResizable(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.ORANGE);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		IPTextField = new JTextField();
		IPTextField.setToolTipText("Enter an IP");
		IPTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		IPTextField.setBounds(236, 248, 165, 36);
		contentPane.add(IPTextField);
		IPTextField.setColumns(10);
		
		nameTextField = new JTextField();
		nameTextField.setText("Hugo");
		nameTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		nameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		nameTextField.setToolTipText("Enter a username");
		nameTextField.setColumns(10);
		nameTextField.requestFocus(false);
		nameTextField.requestFocus();
		//nameTextField.setHorizontalAlignment(JTextField.LEFT);
		nameTextField.setBounds(236, 123, 165, 36);
		contentPane.add(nameTextField);
		
		JLabel nameField = new JLabel("Enter a username:");
		nameField.setHorizontalAlignment(SwingConstants.CENTER);
		nameField.setBounds(236, 76, 165, 47);
		contentPane.add(nameField);
		
		JLabel IP_Field = new JLabel("Enter the IP you want to connect to (or <enter> for localhost):");
		IP_Field.setHorizontalAlignment(SwingConstants.CENTER);
		IP_Field.setBounds(132, 185, 391, 63);
		contentPane.add(IP_Field);
		
		OK_Button = new JButton("OK");
		OK_Button.setBounds(165, 353, 125, 36);
		OK_Button.addActionListener(this);
		contentPane.add(OK_Button);
		
		Cancel_Button = new JButton("Cancel");
		Cancel_Button.setBounds(360, 353, 125, 36);
		Cancel_Button.addActionListener(this);
		contentPane.add(Cancel_Button);
		
		
		JScrollPane scrollPane = new JScrollPane();
		serverTable = new JTable(
				new DefaultTableModel(
					new Object[][]{{}}, new String[]{"Local Server"}
				));
		scrollPane.setViewportView(serverTable);
		scrollPane.setBounds(20, 50, 200, 200);
		contentPane.add(scrollPane);
		
		NetworkScannerWorker scannerWorker = new NetworkScannerWorker(this);
		scannerWorker.execute();
<<<<<<< HEAD
=======
		
>>>>>>> ac91f37e708e7b1c287c511be36e461e552f4d38
		
		JMenu mnNewMenu = new JMenu("Einstellungen");
		mnNewMenu.setBounds(0, 0, 157, 36);
		contentPane.add(mnNewMenu);
		
		JLabel lblIncoming = new JLabel("Incoming");
		mnNewMenu.add(lblIncoming);
		
		getRootPane().setDefaultButton(OK_Button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(OK_Button)){
			start();
		}
		else if(e.getSource().equals(Cancel_Button)){
			System.exit(0);
		}
		
	}

	private void start() {
		System.out.println("ok");
		
		if(nameTextField.getText().equals("")){
			JOptionPane.showMessageDialog(this, "Username can not be empty");
			return;
		}
		
		name = nameTextField.getText();
		
		if(IPTextField.getText().equals(""))
			address = "127.0.0.1";
		else
			address = IPTextField.getText();
		
		socket = new ChatSocket(address, port, this);
	}
	

	public static String getUserName() {
		return name;
	}

	public void complain() {
		JOptionPane.showMessageDialog(this, "IP address seems to be wrong or Server is down");
	}
	
	
	
	  //JmDNS Server discovery
	  
	  public void serviceAdded(ServiceEvent event) {
	  }
	
	  public void serviceRemoved(ServiceEvent event) {
		
	  }
	
	  public void serviceResolved(ServiceEvent event) {
		  DefaultTableModel model = (DefaultTableModel) serverTable.getModel();
		  model.addRow(new Object[]{event.getName()});
	  }

}
