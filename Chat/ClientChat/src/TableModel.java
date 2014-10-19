import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class TableModel extends DefaultTableModel
{
	private JTable table;
	
	public TableModel(Object[][] data, Object[] columns, JTable table)
	{
		super(data,columns);
		this.table = table;
		this.table.setDefaultRenderer(Object.class, new TableCellRenderer(table));
	}
}
