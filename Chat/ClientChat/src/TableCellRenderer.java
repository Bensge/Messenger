import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;


public class TableCellRenderer extends DefaultTableCellRenderer implements ComponentListener
{
	private static final long serialVersionUID = 1L;
	
	private JTable table;
	private Point imageDimension;
	private BufferedImage image;
	private Boolean useNameColor;
	private Boolean useDateColor;
	private Boolean useLightBackgroundColor;
	private ArrayList<Boolean> highlightedRows;
	private Boolean scrollToBottom;
	
	public TableCellRenderer(JTable table)
	{
		this.imageDimension = new Point(10, 10);
		this.table = table;
		this.table.addComponentListener(this);
		this.table.setTableHeader(null);
		this.table.setBackground(new Color(36, 34, 34));
		this.table.setFillsViewportHeight(true);
		this.table.setShowGrid(false);
		this.table.setSelectionModel(new NullSelectionModel());
		this.table.setIntercellSpacing(new Dimension(0, 0));
		this.scrollToBottom = false;
		this.highlightedRows = new ArrayList<Boolean>();
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		if (image != null)
		{
			g.drawImage(image, 0, 0, imageDimension.x, imageDimension.y,null);
		}
		else {
			Color color = new Color(170, 170, 170);
			if (useNameColor)
			{
				String text = this.getText();
				int hash = 0;
				for (int i = 0; i < text.length(); i ++)
				{
					hash += (int)text.charAt(i);
				}
				float hue = (hash % 360)/360.f;
				float sat = 0.6f;
				float bri = 0.6f;
				color = Color.getHSBColor(hue, sat, bri);
			}
			else if (useDateColor)
			{
				color = new Color(138,138,138);
			}
			
			setForeground(color);
			
			if (useLightBackgroundColor)
			{
				this.setBackground(new Color(60, 60, 60));
			}
			else
			{
				this.setBackground(new Color(36, 34, 34));
			}
			
			super.paintComponent(g);
		}
	}
	
	public void setValue(Object value)
	{
    	if (value instanceof BufferedImage)
    	{
    		image = (BufferedImage)value;
    		//setIcon(new ImageIcon(image));
    		setText("");
    	}
    	else
    	{
    		image = null;
    		//setIcon(null);
    		super.setValue(value);
    	}
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		useNameColor = column == 1;
		useDateColor = column == 0;
		
		useLightBackgroundColor = highlightedRows.size() > row && highlightedRows.get(row); 
		
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
	}
	
	@Override
	public Border getBorder() {
		// TODO Auto-generated method stub
		return BorderFactory.createEmptyBorder();
	}
	
	public void relayout()
	{
		int tableWidth = table.getColumn("Message").getWidth();
		int tableHeight = table.getHeight();
		
		int columnCount = table.getColumnCount();
		for (int i = 0; i < table.getRowCount(); i ++)
		{
			for (int j = 0; j < columnCount; j++)
			{
				Object data = table.getModel().getValueAt(i, j);
				if (data instanceof BufferedImage)
				{
					BufferedImage image = (BufferedImage)data;
					
					int height = image.getHeight();
					int width = image.getWidth();
					
					if (image.getHeight() > tableHeight)
					{
						height = tableHeight;
						width = (int)((float)height/image.getHeight()*image.getWidth());
					}
					if (image.getWidth() > tableWidth)
					{
						width = tableWidth;
						height = (int)((float)width/image.getWidth()*image.getHeight());
					}
					imageDimension = new Point(width, height);
					table.setRowHeight(i, height);
				}
			}
		}
	}

	/*
	 * COMPONENT LISTENER CALLBACKS 
	 */
	
	public void componentResized(ComponentEvent e)
	{
		relayout();
		if (scrollToBottom)
		{
			scrollToBottom = false;
			table.scrollRectToVisible(table.getCellRect(table.getRowCount()-1, 0, true));
		}
	}
	
	public void scrollToBottom()
	{
		scrollToBottom = true;
	}
	
	public void setRowHighlighted(int row, Boolean highlighted)
	{
		if (highlightedRows.size() - 1 < row)
		{
			highlightedRows.add(highlighted);
		}
		else
		{
			highlightedRows.set(row, highlighted);
		}
	}
	
	/* Why the fuck do I have to implement those even though I don't need them? Stupid!*/
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
}
