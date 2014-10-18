import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FocusTraversalPolicy;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
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
  private DefaultTableModel model;
  private JTable messageTable;
  public String username;
  private DataSendListener dataListener;

  public GUI() {
	  
	username = Login.getUserName();
	  
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 800, 600);
    getContentPane().setLayout(new BorderLayout());
    
    Box topBox = Box.createHorizontalBox();
    
    Object[] columns = { "Time", "User", "Message" };
    Object[][] rowData = { { "9:99", username, "Hi, der Chat geht jetzt. "} };
    
    messageTable = new JTable(new DefaultTableModel(rowData, columns));
    
    DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
    	public void setValue(Object value) {
	    	if (value instanceof Icon) {
	    		setIcon((Icon) value);
	    		setText("");
	    	} else {
	    		setIcon(null);
	    		super.setValue(value);
	    	}
    	}
    };
    messageTable.setDefaultRenderer(Object.class, r);
    
    messageTable.getColumnModel().getColumn(0).setMinWidth(10);
    messageTable.getColumnModel().getColumn(0).setMaxWidth(70);
    messageTable.getColumnModel().getColumn(0).setWidth(55);
    messageTable.getColumnModel().getColumn(0).setPreferredWidth(55);
    
    messageTable.getColumnModel().getColumn(1).setMinWidth(30);
    messageTable.getColumnModel().getColumn(1).setMaxWidth(100);
    messageTable.getColumnModel().getColumn(1).setWidth(60);
    messageTable.getColumnModel().getColumn(1).setPreferredWidth(60);
    
    //messageTable.setMaximumSize(new Dimension(9999, 300));
    JScrollPane messageTableScroller = new JScrollPane(messageTable);
    messageTableScroller.setBackground(Color.WHITE);
    messageTableScroller.setMinimumSize(new Dimension(350, 200));
    messageTableScroller.setPreferredSize(new Dimension(500, 220));
    
    topBox.add(messageTableScroller);
    
    Box bottomBox = Box.createHorizontalBox();
    
    textField = new JTextField();
    //50 is actually too small, but the group and with it the text field always has at least the button's height.
    textField.setMaximumSize(new Dimension(9999, 50));
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
					if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
					{	
						int reply = JOptionPane.showConfirmDialog(textField, "Do you want to send the dropped file?", "File Transfer", JOptionPane.YES_NO_OPTION);
						if (reply == JOptionPane.YES_OPTION)
						{
							BufferedImage image = ImageIO.read(f);
							dataListener.sendObject(image);
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
    
    btnSend = new JButton("Send");
    //For send on enter button press.
    getRootPane().setDefaultButton(btnSend);
    btnSend.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
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
	  model = (DefaultTableModel) messageTable.getModel();
	  model.addRow(new Object[]{date, name, text});
  }
  
  public void addEntry(String date, String name, Object o){
	  model = (DefaultTableModel) messageTable.getModel();
	  model.addRow(new Object[]{date, name, o});
  }

}