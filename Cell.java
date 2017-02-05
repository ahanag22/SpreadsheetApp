package app;

import java.util.LinkedList;
import java.util.Stack;

/**
 * A class that represents each cell of the spreadsheet.
 */
public class Cell { 
	
	/**
	 * The variable that holds the formula String.
	 */
	private String formula;	
	
	/**
	 * The variable that holds the in value of the cell.
	 */
	private int value;
	
	/**
	 * The variable that holds the int unique value of the cell. used to
	 * identify the cell.
	 */
	private int uniqueId;
	
	/**
	 * The variable that holds the inDegree of the cell.
	 */
	private int inDegree;
	
	/**
	 * The list of cells that depends on this cell.
	 */
	private LinkedList<Cell> dependsOnMe; 
	
	/**
	 * The expression tree below represents the formula 	
	 */
	private ExpressionTree expressionTree;
	
	/**
	 * Constructor of the cell
	 */
	public Cell(){
		this.formula = null;
		this.value = 0;
		this.uniqueId = -1;
		this.inDegree = 0;
		dependsOnMe = new LinkedList<Cell>();
	}
	
	/**
	 * A method that sets an unique id of this node.
	 * @param id the unique id of this node.
	 */
	public void Intialize(int id){
		this.uniqueId = id;
	}
	
	/**
	 * A method that sets the formula of this node.
	 * @param theFormula the new formula of the node that needs to set.
	 */
	public void setFormula(String theFormula){
		this.formula = theFormula;
	}
	
	/**
	 * A method that returns the formula of this node.
	 * @return the formula of this node.
	 */
	public String getFormula() {
		return formula;
	}
	
	/**
	 * A method that returns the an unique id of this node.
	 * @return the unique id of this node.
	 */
	public int getUniqueId(){
		return this.uniqueId;
	}
	
	/**
	 * A method that returns the value of this node.
	 * @return the value of this node.
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * A method that returns the Indegree of this node. 
	 * @return the inDegree of this Node. 
	 */
	public int getIndegree() {
		return inDegree;
	}
	
	/**
	 * A method to increment the in-degree of the node.
	 */
	public void incrementInDegree() {
		inDegree++;
	}
	
	/**
	 * A method to decrement the in-degree of the node.
	 */
	public void decrementInDegree() {
		inDegree--;
	}
	
	/**
	 * This method replaces the current inDegree with a new one.
	 * 
	 * @param newInDegree The specified new inDegree
 	 */
	public void setInDegree(int newInDegree) {
		this.inDegree = newInDegree;
	}
	
	/**
	 * A method to add an edge to this node. 
	 * @param theCellToAdd the Cell to add to this nodes dependsOnMe List.
	 */
	public void addEdge(Cell theCellToAdd) {
		dependsOnMe.add(theCellToAdd);
	}
	
	/**
	 * A method that 
	 * @param theCellToRemove
	 */
	public void removeEdge(Cell theCellToRemove) {
		dependsOnMe.remove(theCellToRemove);
		this.decrementInDegree();
	}
	
	/**
	 * A method to get the list of nodes that depend on this node.
	 * @return the list of nodes that depend on this node. 
	 */
	public LinkedList<Cell> getDependsOnMeList() {
		return dependsOnMe;
	}

	/**
	 * This method returns the dependency list of the cells. Cells
	 * on those this cell depends on.
	 * @return the list of the cells.
	 */
	public LinkedList<CellToken> getDependencyList() {
		LinkedList<CellToken> dependencyList = new LinkedList<CellToken>();
		if (expressionTree != null) {
		this.expressionTree.getRoot().PopulateAllCellTokensInTree(dependencyList);
		}

		return dependencyList;
	}

	/**
	 * This method clears the list of cells that it depends on in case of
	 * overwriting the cell.
	 */
    public void resetDependencyList() {
		expressionTree = null;
	}

	/**
	 * A method that get the stack of tokens from formula and from the 
	 * stack of tokens populate the expression tree and then walk through 
	 * the expression tree and determine if a cycle exists.
	 * 
	 * @param spreadsheet the Spreadsheet
	 * @return false if the formula creates a circular dependency, returns true otherwise. 
	 */
	public boolean Evaluate (Spreadsheet spreadsheet){
		// Get the stack of tokens from formula
		// From the stack of tokens populate the expression tree.
		if(this.formula == null || this.formula.length() <= 0){
			this.value = 0;
			return true;
		}
		Stack<Token> cellFormula =  Util.getFormula(formula);
		this.expressionTree = new ExpressionTree();
		this.expressionTree.BuildExpressionTree(cellFormula);
		
		// Now walk the Expression tree and determine if a cycle exists.
		if(this.FindIfCellWithIdFound(spreadsheet, uniqueId)){
			System.out.println("Error: Creats a circular dependency.");
			this.expressionTree = null;
			return false;
		}				
	
		this.value = this.expressionTree.Evaluate(spreadsheet);
		return true;
	}
	
	/**
	 * 
	 * 
	 * @param s is the spreadsheet
	 * @param cellId is the unique id of each cell.
	 * @return checking if the cell with same id is present in the expression tree or not.
	 */
	public boolean FindIfCellWithIdFound(Spreadsheet s, int cellId){
		 if(this.expressionTree != null 
			&& this.expressionTree.FindIfCellExists(s, cellId)) {
			 return true;
		 }
		 return false;
	}
}