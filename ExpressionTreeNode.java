package app;

import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * This class represents each expression tree node.
 * @author 
 *
 */
public class ExpressionTreeNode { 
	/**
	 * Constructor of ExpressionTreeNode.
	 * @param token is Token
	 * @param leftSubtree is an ExpressionTreeNode.
	 * @param rightSubtree is an ExpresionTreeNode.
	 */
	public ExpressionTreeNode(
			Token token,
			ExpressionTreeNode leftSubtree,
			ExpressionTreeNode rightSubtree) {
		this.token = token;
		this.left = leftSubtree;
		this.right = rightSubtree;
	}
	
	private Token token;
	
	private ExpressionTreeNode left; 
	private ExpressionTreeNode right;
	/**
	 * This method populates all cell tokens in tree.
	 * @param listToPopulate is list of cell token.
	 */
	public void PopulateAllCellTokensInTree(LinkedList<CellToken> listToPopulate){
		if(this.isCellTokenNode()) {
			listToPopulate.add((CellToken) this.getToken());
		}
	    
		if(this.left != null) this.left.PopulateAllCellTokensInTree(listToPopulate); 
	    if(this.right != null) this.right.PopulateAllCellTokensInTree(listToPopulate);
	    
	    return;
		
	}
	/**
	 * A method that returns token of this expression node.
	 * @return token of this expression node.
	 */
	public Token getToken (){
		return this.token;
	}
	/**
	 * This method returns left subtree.
	 * @return left subtree.
	 */
	public ExpressionTreeNode getLeft() {
		return this.left;
	}

	/**
	 * This method returns right subtree.
	 * @return right subtree.
	 */
	public ExpressionTreeNode getRight() {
		return this.right;
	}
	/**
	 * This method returns the value of this node.
	 * @param spreadsheet is the Spreadsheet.
	 * @return returns the value of this ExpressionTreeNode in int.
	 */
	public int getValue(Spreadsheet spreadsheet) {
		if(token instanceof LiteralToken) {
			LiteralToken l = (LiteralToken) token;
			return l.getIntValue();
		}
		else if(token instanceof CellToken) {
			CellToken c = (CellToken) token;
			Cell myCell = spreadsheet.getCellFromCellToken(c);
			myCell.Evaluate(spreadsheet);
			return myCell.getValue();
		}
		else{
			OperatorToken o = (OperatorToken) token;
			char operation = o.getOperatorToken();
			int val1 = left.getValue(spreadsheet);
			int val2 = right.getValue(spreadsheet);
			int result = 0;
			if(operation == '-'){
			    result = val1 - val2;
		    }
		    else if(operation == '+') {
		    	result = val1 + val2; 
		    }	
		    else if(operation == '*') {
		    	result = val1 * val2; 
		    }
		    else {
		    	if (val2 != 0) {
		    		result = val1 / val2; 
		    	} else {
		    		JOptionPane.showMessageDialog(new JPanel(),  "Careful, you are trying to divide by zero. This "
		    				+ "results in an error. ", "Dividing by Zero", JOptionPane.ERROR_MESSAGE);
		    		return 0;
		    	}
		    }
	    return result;
		}
	}
	/**
	 * Checking if the token is cell token or not.
	 * @return true if the token is a cell token, returns false otherwise.
	 */
	private boolean isCellTokenNode(){
		 if(this.token instanceof CellToken){
			 return true;
		 }		 
		 return false;
	}
	/**
	 * Checking for cell with same id. 
	 * @param s is the Spreadsheet.
	 * @param uniqueId is the unique id of each cell.
	 * @return true if found, returns false otherwise.
	 */
	public boolean RecursiveFindCellById(Spreadsheet s, int uniqueId){
		if(this.isCellTokenNode()){
			Cell actualCell = s.getCellFromCellToken((CellToken)this.token);
			if(actualCell.getUniqueId() == uniqueId){
				return true;
			}
			else {
				return actualCell.FindIfCellWithIdFound(s, uniqueId);
			}
		}
		
		boolean found = false;
		
		if(this.left != null)
		{
			found = this.left.RecursiveFindCellById(s, uniqueId);
			
			if(found){
				return true;
			}					
		}
		
		if(this.right != null){
			return this.right.RecursiveFindCellById(s, uniqueId);
		}
		
		return false;
	}
}
