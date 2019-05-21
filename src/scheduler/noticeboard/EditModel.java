package scheduler.noticeboard;

import javax.swing.table.AbstractTableModel;

public class EditModel extends AbstractTableModel {
	String[] columnName =  new String[1];
	Object data[][] = new Object[1][1];
	EditPage editPage;

	public EditModel(EditPage editPage) {
		this.editPage = editPage;
	}

	public int getColumnCount() {

		return columnName.length;
	}

	@Override
	public int getRowCount() {

		return data.length;
	}

	@Override
	public String getColumnName(int col) {

		return columnName[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

}
