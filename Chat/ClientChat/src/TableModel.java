import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class TableModel extends DefaultTableModel
{
	private JTable table;
	private TableCellRenderer renderer;
	
	public TableModel(Object[][] data, Object[] columns, JTable table)
	{
		super(data,columns);
		this.table = table;
		this.renderer = new TableCellRenderer(table);
		this.table.setDefaultRenderer(Object.class, renderer);
	}
	
	@Override
	public void addRow(Object[] rowData) {
		addRow(rowData, false);
	}
	
	public void addRow(Object[] rowData, Boolean highlightRow) {
		renderer.setRowHighlighted(this.getRowCount(), highlightRow);
		super.addRow(rowData);
		renderer.scrollToBottom();
	}

	public TableCellRenderer getRenderer(){
		return renderer;
	}
}
