package app;

import java.util.Stack;

import javax.swing.table.AbstractTableModel;

/**
 * This class is what builds the GUI of the spreadsheet.
 */
public class MyDataModel extends AbstractTableModel{	
	/**
	 * The spreadsheet object is a field so that we can access the current
	 * state of all the cells.
	 */
	private Spreadsheet theSpreadsheet;
		
	/**
	 * Default constructor
	 */
	public MyDataModel() {
	}
	
	/**
	 * Constructor that takes in the spreadsheet.
	 * @param theSpreadsheet
	 */
	public MyDataModel(Spreadsheet theSpreadsheet) {
		this.theSpreadsheet = theSpreadsheet;
	}
	
	/**
	 * Gets the number of columns.
	 */
	@Override
	public int getColumnCount() {
		return theSpreadsheet.getNumCols();
	}

	/**
	 * Gets the number of rows.
	 */
	@Override
	public int getRowCount() {
		return theSpreadsheet.getNumRows();
	}

	/**
	 * This method is called whenever a table update is made. It basically
	 * repaints each cell with its corresponding data.
	 * Note that it is called after setValueAt method.
	 * 
	 * @param rowIndex The current row.
	 * @param columnIndex The current Column.
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Cell[][] data = theSpreadsheet.getAllCells();
		int value = data[rowIndex][columnIndex].getValue();
		String formula = data[rowIndex][columnIndex].getFormula();
		if (value == 0 && formula == null) {
			fireTableCellUpdated(rowIndex, columnIndex);
			return "";
		} else {
			fireTableCellUpdated(rowIndex, columnIndex);
			return value;
		}
		
	}
	
	/**
	 * This method is called whenever a cell is changed. The new contents of
	 * the cell is stored to value and the row and column information is stored in their
	 * two corresponding int variables.
	 * 
	 * @param value The new content of the current cell.
	 * @param rowIndex The row of the current cell.
	 * @param columnInex The column of the current cell.
	 */
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		CellToken tempCellToken = new CellToken(columnIndex, rowIndex + 1);
		Stack<Token> expTreeTokenStack;
		expTreeTokenStack = Util.getFormula((String)value);
		theSpreadsheet.changeCellFormulaAndRecalculate(tempCellToken, expTreeTokenStack);	
	}
		
	/**
	 * A boolean called by the data model of which cells are editable.
	 * In this case, all are.
	 * 
	 * @param row The row of the current cell
	 * @param col The column of the current cell.
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
    }
	
	
	
}
