import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class GUI extends JFrame {
	private JTextField textField;
	private JButton btnSend;
	private boolean canSend = false;
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
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 782, 493);
		getContentPane().add(panel);
		
		JLabel lblNewLabel = new JLabel("New label");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 494, 782, 59);
		getContentPane().add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		textField = new JTextField();
		panel_1.add(textField);
		textField.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_1.add(btnSend);
		pack();
	}
	
	public String getText(){
		canSend = false;
		return textField.getText().toString();
	}

	public boolean canSend() {
		return canSend;
	}
	
	public void setSendButtonListener(ActionListener listener)
	{
		if (btnSend.getActionListeners().length > 0)
			btnSend.removeActionListener(btnSend.getActionListeners()[0]);
		
		btnSend.addActionListener(listener);
	}
}
