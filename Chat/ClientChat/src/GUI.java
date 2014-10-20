import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.Renderer;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class GUI extends JFrame {

private static final long serialVersionUID = 1L;
private JTextField textField;
  private JButton btnSend;
  private JButton emojiButton;
  private TableModel model;
  private JTable messageTable;
  public String username;
  private DataSendListener dataListener;

  public GUI() {
	  
	setTitle("Messenger");
	  
	username = Login.getUserName();
	  
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 800, 600);
    getContentPane().setLayout(new BorderLayout());
    
    Box topBox = Box.createHorizontalBox();
    
    Object[] columns = { "Time", "User", "Message" };
    Object[][] rowData = {};
    
    
    messageTable = new JTable(null);
    TableModel model = new TableModel(rowData, columns, messageTable);
    messageTable.setModel(model);
    
    
    messageTable.getColumnModel().getColumn(0).setMinWidth(10);
    messageTable.getColumnModel().getColumn(0).setMaxWidth(70);
    messageTable.getColumnModel().getColumn(0).setWidth(45);
    messageTable.getColumnModel().getColumn(0).setPreferredWidth(45);
    
    messageTable.getColumnModel().getColumn(1).setMinWidth(30);
    messageTable.getColumnModel().getColumn(1).setMaxWidth(100);
    messageTable.getColumnModel().getColumn(1).setWidth(50);
    messageTable.getColumnModel().getColumn(1).setPreferredWidth(50);
    
    //messageTable.setMaximumSize(new Dimension(9999, 300));
    JScrollPane messageTableScroller = new JScrollPane(messageTable);
    messageTableScroller.setBackground(new Color(35, 34, 34));
    messageTableScroller.getViewport().setBackground(new Color(35, 34, 34));
    messageTableScroller.setMinimumSize(new Dimension(350, 200));
    messageTableScroller.setPreferredSize(new Dimension(500, 220));
    
    topBox.add(messageTableScroller);
    
    Box bottomBox = Box.createHorizontalBox();
    bottomBox.setOpaque(true);
    bottomBox.setBackground(new Color(90, 90, 90));
    
    textField = new JTextField();
    //50 is actually too small, but the group and with it the text field always has at least the button's height.
    textField.setMaximumSize(new Dimension(9999, 50));
    textField.setBackground(new Color(190, 190, 190));
    textField.setText("\uE415" + "\uE056" + "\uE057" + "\u263a" + "abcd" + "\uF8FF");
    //textField.setFont(new Font("Apple Color Emoji", Font.PLAIN, 10));
    DropTarget dt = new DropTarget(textField, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetListener() {
		
		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {
			// TODO Auto-generated method stub
			
		}
		
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
							//for images
							if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
							{
								BufferedImage image = ImageIO.read(f);
								dataListener.sendObject(image);
							}
							//if not an image
							else{
								dataListener.sendObject(f);
								
							}
					
						
					dtde.dropComplete(true);
					return;
					
					
				}
			}
				dtde.rejectDrop();
				dtde.dropComplete(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("scheisse");
				dtde.dropComplete(false);
			}
		}
		
		@Override
		public void dragOver(DropTargetDragEvent dtde) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void dragExit(DropTargetEvent dte) {
			// TODO Auto-generated method stub
			
		}
		
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
    
    Box verticalBox = Box.createVerticalBox();
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


  public void addEntry(String date, String name, String text){
	  model = (TableModel) messageTable.getModel();
	  
	  Boolean highlighted = name.equals("Server");
	  model.addRow(new Object[]{date, name, text},highlighted);
  }
  
  public void addEntry(String date, String name, BufferedImage image){
	  model = (TableModel) messageTable.getModel();
	  model.addRow(new Object[]{date, name, image});
  }

}