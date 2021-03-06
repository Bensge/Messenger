import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import Common.MessageUserActionPacket.Action;


public class GUI extends JFrame implements ActionListener{

private static final long serialVersionUID = 1L;
  private JTextField textField;
  private JButton btnSend;
  private JButton emojiButton;
  public TableModel messagesModel;
  private JTable messageTable;
  private JTable userTable;
  public TableModel userModel;
  private ArrayList<String> users;
  public String username;
  
  public final Color darkColor = new Color(0, 0, 0);
  public final Color brightColor = new Color(255, 255, 255);
  private JScrollPane messageTableScroller;
  private JScrollPane userTableScroller;
  private JMenuBar menuBar;
  private JMenu look, other;
  private JMenuItem changeGUI, setMediaFolder;
  private JCheckBoxMenuItem mute;
  private Box bottomBox, verticalBox;
  
  private boolean isMuted = false;
  private SettingsListener settingsListener;
  private DataSendListener dataListener;

  public GUI() {
	
	if (System.getProperty("os.name").startsWith("Mac"))
	{
		System.setProperty("apple.laf.useScreenMenuBar", "true");
	}
	settingsListener = new Settings(this);
	
	username = Login.getUserName();
	
	setTitle("Messenger: " + username);
	  
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 800, 600);
    getContentPane().setLayout(new BorderLayout());
    
    //Box topTopBox = Box.createHorizontalBox();
    Box topBox = Box.createHorizontalBox();
    
    menuBar = new JMenuBar();
    look = new JMenu("Design");
    other = new JMenu("Other");
    changeGUI = new JMenuItem("Change Design");
    setMediaFolder = new JMenuItem("Set Media Folder");
    mute = new JCheckBoxMenuItem("Mute all Sounds");
    
    mute.addActionListener(this);
    changeGUI.addActionListener(this);
    setMediaFolder.addActionListener(this);
    
    look.add(changeGUI);
    other.add(setMediaFolder);
    other.add(mute);
    
    //menuBar.add(changeGui);
    menuBar.add(look);
    menuBar.add(other);
    
    menuBar.setBackground(brightColor);
   // topTopBox.add(menuBar);
    //topTopBox.set
    setJMenuBar(menuBar); 
    changeGUI.setBackground(brightColor);
    
    Object[] columns = { "Time", "User", "Message" };
    Object[][] rowData = {};
    
    
    messageTable = new JTable(null);
    messagesModel = new TableModel(rowData, columns, messageTable);
    messageTable.setModel(messagesModel);
    
    messageTable.getColumnModel().getColumn(0).setMinWidth(10);
    messageTable.getColumnModel().getColumn(0).setMaxWidth(70);
    messageTable.getColumnModel().getColumn(0).setWidth(45);
    messageTable.getColumnModel().getColumn(0).setPreferredWidth(45);
    
    messageTable.getColumnModel().getColumn(1).setMinWidth(30);
    messageTable.getColumnModel().getColumn(1).setMaxWidth(100);
    messageTable.getColumnModel().getColumn(1).setWidth(50);
    messageTable.getColumnModel().getColumn(1).setPreferredWidth(50);
    
    //messageTable.setMaximumSize(new Dimension(9999, 300));
    messageTableScroller = new JScrollPane(messageTable);
    //messageTableScroller.setBackground(new Color(35, 34, 34));
    messageTableScroller.getViewport().setBackground(new Color(35, 34, 34));
    messageTableScroller.setMinimumSize(new Dimension(350, 200));
    messageTableScroller.setPreferredSize(new Dimension(500, 220));
    messageTableScroller.setBackground(brightColor);
	messageTableScroller.getViewport().setBackground(brightColor);
    
    topBox.add(messageTableScroller);
    
    //User table
    
    Object[] userColumns = { "User" };
    users = new ArrayList<String>();
    
    userTable = new JTable(null);
    userModel = new TableModel(rowData, userColumns, userTable);
    userTable.setModel(userModel);
    
    userTableScroller = new JScrollPane(userTable);
    //userTableScroller.setBackground(new Color(35, 34, 34));
    userTableScroller.getViewport().setBackground(new Color(35, 34, 34));
    userTableScroller.setMinimumSize(new Dimension(50, 200));
    userTableScroller.setPreferredSize(new Dimension(80, 220));
    userTableScroller.setBackground(brightColor);
    
    topBox.add(userTableScroller);
    Object[] data = { username };
    users.add(username);
    userModel.addRow(data);
    
    /////////
    
    bottomBox = Box.createHorizontalBox();
    bottomBox.setOpaque(true);
    //bottomBox.setBackground(new Color(90, 90, 90));
    bottomBox.setBackground(new Color(255, 255, 255));
    
    textField = new JTextField();
    //50 is actually too small, but the group and with it the text field always has at least the button's height.
    textField.setMaximumSize(new Dimension(9999, 50));
    textField.setBackground(new Color(190, 190, 190));
    //textField.setText("\uE415" + "\uE056" + "\uE057" + "\u263a" + "abcd" + "\uF8FF");
    //textField.setFont(new Font("Apple Color Emoji", Font.PLAIN, 10));
    DropTarget dt = new DropTarget(textField, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetListener() {
		
		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {	}
		
		@Override
		public void drop(DropTargetDropEvent dtde) {
			Transferable transferable = dtde.getTransferable();
			try {
				/*
				if (!transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
				 	return;
				 */
				dtde.acceptDrop(dtde.getDropAction());
				
				List<File> fileList = (List<File>)transferable.getTransferData(DataFlavor.javaFileListFlavor);
				for (File f : fileList)
				{
					String fileName = f.getName().toLowerCase();
					
						
						int reply = JOptionPane.showConfirmDialog(textField, "Do you want to send the dropped file?", "File Transfer", JOptionPane.YES_NO_OPTION);
						if (reply == JOptionPane.YES_OPTION)
						{
							String date = new SimpleDateFormat("HH:mm").format(new Date());
							
							if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
							{
								//Handle image
								dataListener.sendObject(f, true);
								
								addEntry(date, username, ImageIO.read(f));
							}
							else
							{
								dataListener.sendObject(f, false);
								addEntry(date, username, "You sent file " + fileName);
							}
						}
					
						
					dtde.dropComplete(true);
					return;
				
			}
				dtde.rejectDrop();
				dtde.dropComplete(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("scheisse");
				dtde.dropComplete(false);
			}
			settingsListener.UIChanged();
		}
		
		@Override
		public void dragOver(DropTargetDragEvent dtde) {	}
		
		@Override
		public void dragExit(DropTargetEvent dte) {		}
		
		@Override
		public void dragEnter(DropTargetDragEvent dtde) {
			// TODO Auto-generated method stub
			Transferable transferable = dtde.getTransferable();
			if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				dtde.acceptDrag(dtde.getDropAction());
			}
			else
			{
				dtde.rejectDrag();
			}
		}
		
	});
    bottomBox.add(textField);
    //Simley button with unicode smiley. Let's hope all the default system font have this emoji.
    emojiButton = new JButton("\u263a");
    emojiButton.setPreferredSize(new Dimension(40, 20));
    emojiButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Emoji's, sir!");
		}
	});
    bottomBox.add(emojiButton);
    
    btnSend = new JButton("Send");
    //For send on enter button press.
    getRootPane().setDefaultButton(btnSend);
    btnSend.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(textField.getText().equals(""))
				return;
			
			String date = new SimpleDateFormat("HH:mm").format(new Date());
			
			addEntry(date, username, textField.getText());
			textField.setText(null);
		}
	});
    bottomBox.add(btnSend);
    
    verticalBox = Box.createVerticalBox();
   // verticalBox.add(topTopBox);
    verticalBox.add(topBox);
    verticalBox.add(bottomBox);
    
    getContentPane().add(verticalBox, BorderLayout.CENTER);
    
    pack();
    
    setVisible(true);
    
    textField.requestFocus(false);
    textField.requestFocus();
  }
  
  public String getText(){
    String string = textField.getText().toString();
    return string;
  }

  
  public void setSendButtonListener(ActionListener listener)
  {
    if (btnSend.getActionListeners().length > 1)
      btnSend.removeActionListener(btnSend.getActionListeners()[1]);
    
    btnSend.addActionListener(listener);
  }
  
  public void setDataListener(DataSendListener listener)
  {
	  dataListener = listener;
  }

  public void noteUserAction(Action action, String user)
  {
	  if (action == Action.Join)
	  {
		  users.add(user);
		  Object[] data = { user };
		  userModel.addRow(data);
	  }
	  else if (action == Action.Leave)
	  {
		  int index = users.indexOf(user);
		  userModel.removeRow(index);
		  users.remove(index);
	  }
  }

  public void addEntry(String date, String name, String text){
	  messagesModel = (TableModel) messageTable.getModel();
	  
	  Boolean highlighted = name.equals("Server");
	  messagesModel.addRow(new Object[]{date, name, text},highlighted);
	  
	  if(!name.equals(username) && !isMuted)
		  Toolkit.getDefaultToolkit().beep();
  }
  
  public void addEntry(String date, String name, BufferedImage image){
	  messagesModel = (TableModel) messageTable.getModel();
	  messagesModel.addRow(new Object[]{date, name, image});
	  
  }
  
  public void setGUI(boolean dark)
  {
	  if (dark)
	  {
		  getJMenuBar().setBackground(darkColor);
		  changeGUI.setBackground(darkColor);
		  messageTable.setBackground(darkColor);
		  userTable.setBackground(darkColor);
		  setMediaFolder.setBackground(darkColor);
		  messageTableScroller.setBackground(darkColor);
		  messageTableScroller.getViewport().setBackground(darkColor);
		  bottomBox.setBackground(darkColor);
		  userTableScroller.setBackground(darkColor);
		  verticalBox.setBackground(darkColor);
		  textField.setBackground(darkColor);
		  textField.setForeground(brightColor);
	  }
	  
	  else
	  {
		  getJMenuBar().setBackground(brightColor);
		  changeGUI.setBackground(brightColor);
		  messageTable.setBackground(brightColor);
		  userTable.setBackground(brightColor);
		  setMediaFolder.setBackground(brightColor);
		  messageTableScroller.setBackground(brightColor);
		  messageTableScroller.getViewport().setBackground(brightColor); 
		  bottomBox.setBackground(brightColor);
		  userTableScroller.setBackground(brightColor);
		  verticalBox.setBackground(brightColor);
		  textField.setBackground(brightColor);
		  textField.setForeground(darkColor);
	  }
  }

	@Override
	public void actionPerformed(ActionEvent e) {
		  if(e.getSource() == changeGUI)
			  settingsListener.UIChanged();
		  else if(e.getSource() == setMediaFolder)
		  {
			  JFileChooser fc = new JFileChooser();
			  fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			  int result = fc.showOpenDialog(new JFrame());
			  if (result == JFileChooser.APPROVE_OPTION)
			  {
				  File dir = fc.getSelectedFile();
				  System.out.println("Path: " + dir.getAbsolutePath());
			  }
			  
			  settingsListener.pathChanged();
		  }
		  else if(e.getSource() == mute){
			  isMuted = !isMuted;
		  }
	}
	
}