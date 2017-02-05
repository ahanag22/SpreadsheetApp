package app;
/**

 * A class for containing cell token objects which store the row and column of the cell. 
 * The class has getter and setter methods for the row and column of the cell. 
 */
public class CellToken extends Token { 
	private int column; 
	private int row;
	
	/**
	 * Default constructor.
	 */
	public CellToken() {
	}
	
	/**

	 * Constructor for cell token, which initiates the row and column the the values in the parameters. 
	 * @param column is the number of the column the cell token resides. 
	 * @param row is the number of the row the cell token resides. 
	 */
	public CellToken(int column, int row) {
		this.column = column;
		this.row = row;
	}
	
	/**
	 * A method that returns the row number of this cell token.
	 * @return the row number of this cell token.
	 */
	public int getRow() {
		return this.row;
	}
	
	/**
	 * A method that sets the column number of this cell token.
	 * @param c the column number of this cell token.
	 */
	public void setColumn(int c) {
		this.column = c;		
	}
	
	/**
	 * A method that sets the row number of this cell token.
	 * @param r the row number of this cell token.
	 */
	public void setRow(int r) {
		this.row = r;		
	}
	
	/**
	 * A method that returns the column number of this cell token.
	 * @return the column number of this cell token.
	 */
	public int getColumn() {
		return this.column;
	}
}
