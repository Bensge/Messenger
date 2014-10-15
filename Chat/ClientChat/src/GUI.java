import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FocusTraversalPolicy;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GUI extends JFrame {
  private JTextField textField;
  private JButton btnSend;
  private JTable messageTable;
  /**
   * @wbp.nonvisual location=-290,194
   */
  

  /**
   * Launch the application.
   */

  /**
   * Create the frame.
   */
  public GUI() {
	  
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 800, 600);
    getContentPane().setLayout(new BorderLayout());
    
    Box topBox = Box.createHorizontalBox();
    
    Object[] columns = { "Time", "User", "Message" };
    Object[][] rowData = { { "9:99", "Benno", "Hi, der Chat geht jetzt. "} };
    
    messageTable = new JTable(new DefaultTableModel(rowData, columns));
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
    messageTableScroller.setMinimumSize(new Dimension(350, 200));
    messageTableScroller.setPreferredSize(new Dimension(500, 220));
    
    topBox.add(messageTableScroller);
    
    Box bottomBox = Box.createHorizontalBox();
    
    textField = new JTextField();
    //50 is actually too small, but the group and with it the text field always has at least the button's height.
    textField.setMaximumSize(new Dimension(9999, 50));
    bottomBox.add(textField);
    
    btnSend = new JButton("Send");
    //For send on enter button press.
    getRootPane().setDefaultButton(btnSend);
    btnSend.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			DefaultTableModel model = (DefaultTableModel) messageTable.getModel();
			String date = new SimpleDateFormat("HH:mm").format(new Date());
			model.addRow(new Object[]{date, "Benno", textField.getText()});
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
}
