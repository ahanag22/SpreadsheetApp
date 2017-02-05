package app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


/**
 * This is the class's main function is to store all the edited cells and referenced cells in a list
 * and sort them topologically so that we know which order to evaluate the cells in.
 */
public class SpreadSheetGraph {
	/**
	 * This is the list of cells that have been edited or referenced.
	 */
	private ArrayList<Cell> theCells;

	/**
	 * Default constructor that initializes the list of cells.
	 */
	public SpreadSheetGraph(){
		theCells = new ArrayList<Cell>();
	}
	
	/**
	 * A method that takes the list of Cells(nodes/Vertices) and traverses the list finding Cells with
	 * an in-degree of 0, placing that cell in the beginning of a queue (in the form of a linkedList), 
	 * and decrementing the in-degree of every Cell that depends on it. This is done until every Cell
	 * is in the queue ready to be evaluated. 
	 * 
	 * @param theCells the list of cells that need to be sorted. 
	 * @return the a queue of topologically sorted cells in the form of a LinkedList
	 */
	public LinkedList<Cell> topologicalSort(Cell theCell){
		addToList(theCell);
		ArrayList<Integer> cellInDegrees = getInDegrees();
		Set<Cell> myObservedCellsSet = new HashSet<Cell>();
		int numberOfCellsChecked = 0;
		LinkedList<Cell> aTopSortedList = new LinkedList<Cell>(); 
		int numberOfCells = theCells.size();
		while (numberOfCellsChecked < numberOfCells) {
			for(Cell cellToCheck: theCells) {
				if (cellToCheck.getIndegree() == 0 && !myObservedCellsSet.contains(cellToCheck)) {
					aTopSortedList.addLast(cellToCheck);
					myObservedCellsSet.add(cellToCheck);
					for (Cell cellToDecrementIndegree: cellToCheck.getDependsOnMeList()) {
						cellToDecrementIndegree.decrementInDegree();
						numberOfCellsChecked = 0;
					}
				}
				numberOfCellsChecked++;
			}
			
		}
		resetInDegrees(cellInDegrees);
		return aTopSortedList;
	}
	
	/**
	 * This method is for the cells that are only referenced and not yet edited.
	 * Those cells are only added to the queue of cells to be evaluated.
	 * 
	 * @param newCell the cell that will be added to theCells list
	 */
	public void addToList(Cell newCell) {
		boolean exists = false;
		for (Cell c : theCells) {
			if (c.getUniqueId() == newCell.getUniqueId()) {
				exists = true;
			}
		}
		if (!exists) {
			theCells.add(newCell);
		}	
	}
	
	/**
	 * This method is for the cells that were referenced in the most recently
	 * added invalid cell that was part of a cycle.  This method removes the 
	 * edited cell and referenced cells from the queue.
	 * @param theCell The Cell being removed.
	 */
	public void removeFromList(Cell theCell) {
		theCell.resetDependencyList();
		theCell.decrementInDegree();
		theCells.remove(theCell);
	}
	
	/**
	 * This method gets the list of all the current inDegrees of the current
	 * list of cells to be evaluated so that after the list is topologically 
	 * sorted, their respective inDegrees will be restored.
	 * 
	 * @return An ArrayList of integers representing the inDegrees of each cell to be evaluated.
	 */
	public ArrayList<Integer> getInDegrees() {
		ArrayList<Integer> cellInDegrees = new ArrayList<Integer>();
		for (Cell theCell : theCells) {
			cellInDegrees.add(theCell.getIndegree());
		}
		return cellInDegrees;
	}
	
	/**
	 * This method goes through the list of cells to be evaluated and restores
	 * their respective inDegrees to what they were before they all got decremented
	 * to zero from the topological sort method.
	 * 
	 * @param cellInDegrees The integer ArrayList of inDegrees to be restored to the corresponding cells.
	 */
	public void resetInDegrees(ArrayList<Integer> cellInDegrees) {
		for (int i = 0; i < theCells.size(); i++) {
			Cell tempCell = theCells.get(i);
			tempCell.setInDegree(cellInDegrees.get(i));			
		}
	}
	
	/**
	 * This method adds the 'dest' cell to the 'source' cell's dependsOnMe list of 
	 * cells.  It also increments the inDegree of the 'dest' cell. 
	 * 
	 * @param source The cell that is depended on
	 * @param dest The cell that depends on the 'source' cell.
	 */
	public void addToDependsOnMe(Cell source, Cell dest){
		source.addEdge(dest);
		dest.incrementInDegree();
	}	
	
    public void removeEdge(Cell source , Cell dest){
		source.removeEdge(dest);
	}
    
	/**
	 * This method is for when a cell is overwritten.  We remove all the dependencies that were added
	 * by the previous state of the cell (if the old formula referenced other cells) and the cell
	 * is refreshed as if it was an empty cell to begin with.
	 * 
	 * @param overwrittenCell The cell that was overwritten.
	 */
	public void overwriteCell(Cell overwrittenCell) {
		boolean exists = false;
		Cell tempCell = null;
		for (Cell eachCell : theCells) {
			if (eachCell.equals(overwrittenCell)) {
				tempCell = eachCell;
				exists = true;
			}
		}
		if (exists) {
			theCells.remove(tempCell);
		}
	}
}
