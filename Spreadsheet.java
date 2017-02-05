package app;


import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * This is the main back-end class code. It holds the current state of all the cells
 * in the spreadsheet and is the central point of altering them.
 */
public class Spreadsheet {

	/**
	 * The field that holds the two dimensional array of all the cells.
	 */
	private Cell[][] allCells;

	/**
	 * The number of rows in the sheet.
	 */
	private int rows;
	
	/**
	 * The number of columns in the sheet.
	 */
	private int cols;
	
	/**
	 * The Spreadsheet graph.
	 */
	SpreadSheetGraph sG;
	
	/**
	 * Default constructor
	 */
	public Spreadsheet() {
	}
	
	/**
	 * The constructor for the spreadsheet.
	 * 
	 * @param r The number of rows in the spreadsheet.
	 * @param c The number of columns in the spreadsheet.
	 */
	public Spreadsheet(int r, int c) {
		this.rows = r;
		this.cols = c;
		allCells = new Cell[r][c];
		//int numCells = r*c;
		this.sG = new SpreadSheetGraph();
		
		for(int i = 0; i < r; i++){
			for(int j = 0; j < c; j++){
				int uniqueId = i * c + j;
				allCells[i][j] = new Cell();
				allCells[i][j].Intialize(uniqueId);
			}
		}
	}
	
    /**
     * Getter for the Cell array but in their value form.
     * 
     * @return The array of values for every cell in the spreadsheet.
     */
	public String[][] getValArray() {
		int val = 0;
		String [][] newArr = new String[rows][cols];

		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++) {
			    allCells[i][j].Evaluate(this);
			    val = allCells[i][j].getValue();
			    if (val != 0) {
			    	newArr[i][j] = val + "";
			    } else {
			    	newArr[i][j] = "";
			    }
			    
			}
		}
		return newArr;
	}
	
	/**
	 * Prints the values of all the cells in a grid like format
	 * in the console.
	 */
	public void printValues() {
		int val = 0;
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++) {
			    allCells[i][j].Evaluate(this);
			    val = allCells[i][j].getValue();
			    System.out.print(val + " ");
			}
			System.out.println();
		}
	}
	
    /**
     * Gets the number of rows in the sheet.
     * 
     * @return The number of rows in the sheet.
     */
	public int getNumRows() {
		return this.rows;
	}
    
	/**
	 * Gets the number of columns in the sheet.
	 * 
	 * @return The number of columns in the sheet.
	 */
    public int getNumCols() {
		return this.cols;
	}
    
    /**
     * Prints the formula of the specified cell token of a cell.
     * 
     * @param cellToken The specified cell token.
     */
	public void printCellFormula(CellToken cellToken) {
		Cell c = this.getCellFromCellToken(cellToken);
		String s = c.getFormula();
		System.out.println(s);
		
	}
	
    /**
     * Prints all the formulas of all cells in grid like format in the console.
     */
	public void printAllFormulas() {
		String s = "";
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++) {
			    s = allCells[i][j].getFormula();
			    System.out.print(s + " ");
			}
			System.out.println();
		}	
	}
	
    /**
     * Getter for the two dimensional list of cells.
     * 
     * @return The list of cells.
     */
    public Cell[][] getAllCells() {
        return this.allCells;
    }
    
    /**
     * Gets the cell token.
     * 
     * @param inputCell
     * @param i
     * @param cellToken
     * @return
     */
	public CellToken getCellToken(String inputCell, int i, CellToken cellToken) {
		int colNum = (int)inputCell.charAt(0) - 'A';
		int rowNum = inputCell.charAt(1) - '0';
		
		return new CellToken(colNum, rowNum);		
	}
	
	/**
	 * Gets the cell object from the cell token.
	 * 
	 * @param cellToken The specified cell token.
	 * @return The cell that is represented by the specified cell token.
	 */
	public Cell getCellFromCellToken(CellToken cellToken){
		return this.allCells[cellToken.getRow() - 1][cellToken.getColumn()];
	}
	
	/**
	 * This method is called whenever an update to a cell is made.  It updates the formula
	 * in the cell object with the new formula, makes sure that it didn't create a cycle, and
	 * then runs it through the topological sort method.  Then with the list that is spit out,
	 * all cells that have been edited or referenced are evaluated in order.
	 * 
	 * @param cellToken The cell that has been changed.
	 * @param expTreeTokenStack The formula in postfix expression tree stack format.
	 */
	public void changeCellFormulaAndRecalculate(CellToken cellToken, Stack<Token> expTreeTokenStack) {
		// Determine the list of other cells this cell now will depend on.
		// Add edge to the spreadsheet graph. Remove old edges.
		// Then Locate the Cell this celltoken represents.
		// Call the cell.Evaluate();
		Cell c = this.getCellFromCellToken(cellToken);
		String oldFormula = c.getFormula();	
		if (oldFormula != null) {
			//if the cell in question depended on any other cell,
			//it should decrement the inDegree of the current Cell
			//this method should also remove the cell from the topological sort list
			overwriteCell(c, oldFormula);
		}
		changeAndRecalc(cellToken, expTreeTokenStack, c);
		LinkedList<Cell> topSortedArr = sG.topologicalSort(c);
		for (Cell eachCell: topSortedArr) {
			eachCell.Evaluate(this);
		}		
	}
	
	/**
	 * This method is called when a cell is overwritten.  Dependencies are reset and
	 * affected inDegrees are reverted.
	 * 
	 * @param c The cell that was overwritten.
	 * @param oldFormula The formula prior to being overwritten.
	 */
	private void overwriteCell(Cell c, String oldFormula) {
		LinkedList<CellToken> cellsDependedOn = c.getDependencyList();
		for (CellToken eachCell: cellsDependedOn) {
			Cell tempC = getCellFromCellToken(eachCell);
		}
		
		//removes the cell from the top sort list
		sG.overwriteCell(c);		
	}
	
	/**
	 * This just a helper method that is called to unravel the formula from the expression tree stack.
	 * It gets the original formula from the stack and then assigns that formula to the cell that was changed.
	 * 
	 * @param cellToken The cell token of the changed cell.
	 * @param expTreeTokenStack The formula in postfix expression tree format.
	 * @param currentCell The current cell object that was changed.
	 */
	public void changeAndRecalc(CellToken cellToken, Stack<Token> expTreeTokenStack, Cell currentCell) {
		Boolean poly = false;
		Stack<Token> operatorStack= new Stack<Token>();
		
        //sets the cell's formula to the new formula
		String newFormula = "";
		char oT = '\0';
		int cellValue;
		while (!expTreeTokenStack.isEmpty()) {
			if (expTreeTokenStack.peek() instanceof CellToken) {
				CellToken temp = (CellToken) expTreeTokenStack.pop();
				
				Cell tempCell = getCellFromCellToken(temp);
				sG.addToDependsOnMe(tempCell, currentCell);
				sG.addToList(tempCell);
				
				char col = (char)(temp.getColumn() + 65);
				int row = temp.getRow();
				newFormula = "" + col + row + newFormula;
				if (poly) {
					newFormula = " " + oT + " " + newFormula;
					poly = false;
				}
			} else if (expTreeTokenStack.peek() instanceof OperatorToken) {
				while(expTreeTokenStack.peek() instanceof OperatorToken) {
					operatorStack.push((OperatorToken) expTreeTokenStack.pop());
				}
				OperatorToken temp = (OperatorToken)operatorStack.pop();
				oT= (char)temp.getOperatorToken();
				poly = true;
			} else {
				LiteralToken temp = (LiteralToken)expTreeTokenStack.pop();
				cellValue = temp.getIntValue();
				if (cellValue < 0) {
					newFormula = "(" + cellValue + ")" + newFormula;
				} else {
					newFormula = "" + cellValue + newFormula;
				}
				if (poly) {
					newFormula = " " + oT + " " + newFormula;
					if (operatorStack.isEmpty()) {
						poly = false;
					} else {
						OperatorToken tempOT = (OperatorToken)operatorStack.pop();
						oT = (char)tempOT.getOperatorToken();
					}
				}
			}
		}
		

		String oldFormula = currentCell.getFormula();		
		currentCell.setFormula(newFormula); //new cell formula 
		
		// Detect for dividing by zero
		
		
		// Detect for cycles		


		if(!currentCell.Evaluate(this)){
			currentCell.setFormula(oldFormula);
			sG.removeFromList(currentCell);
			JOptionPane.showMessageDialog(new JPanel(),  "Careful, we found one or more circular references in your workbook that might cause your "
					+ "formulas to calculate incorrectly.\n\nFYI: A circular reference can be a formula that refers to its own cell value, or "
					+ "refers to a cell dependent on its own cell value. ", "Circular Dependency", JOptionPane.ERROR_MESSAGE);
			
		}
	}
}
