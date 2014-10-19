import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class TableCellRenderer extends DefaultTableCellRenderer implements ComponentListener
{
	private static final long serialVersionUID = 1L;
	
	private JTable table;
	private Point imageDimension;
	private BufferedImage image;
	
	
	public TableCellRenderer(JTable table)
	{
		this.imageDimension = new Point(10, 10);
		this.table = table;
		this.table.addComponentListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		ImageIcon i;
		if ((i = (ImageIcon) getIcon()) != null)
		{
			System.out.println("Drawing image...");
			g.drawImage(image, 0, 0, imageDimension.x, imageDimension.y,null);
		}
		else {
			super.paintComponent(g);
		}
	}
	
	public void setValue(Object value)
	{
    	if (value instanceof BufferedImage)
    	{
    		image = (BufferedImage)value;
    		setIcon(new ImageIcon(image));
    		setText("");
    	}
    	else
    	{
    		setIcon(null);
    		super.setValue(value);
    	}
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
		System.out.println("Hi redraw pls");
		relayout();
	}
	
	/* Why the fuck do I have to implement those even though I don't need them? Stupid!*/
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
}
