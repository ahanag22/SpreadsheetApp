package app;

import java.util.Stack;

/**
 * This class holds the expression tree. 
 */
public class ExpressionTree { 
	/**
	 * The root of the expression tree.
	 */
	private ExpressionTreeNode root;
	/**
	 * This method returns this expressionTreeNode.
	 * @return the expressionTreeNode.
	 */
	public ExpressionTreeNode getRoot(){
	    return this.root;
	}
	/**
	 * This method returns the value of the node.
	 * @param spreadsheet is Spreadsheet.
	 * @return the value of the node.
	 */
    public int Evaluate(Spreadsheet spreadsheet) {
		return root.getValue(spreadsheet);
	}
	/**
	 * This method builds the exprssionTree from the stack of tokens and returns the expressionTree.
	 * @param s stack of ExpressionTreeTokens 
	 * @return the ExpressionTree.
	 */
	public ExpressionTreeNode GetExpressionTree(Stack<Token> s) { 
		ExpressionTreeNode returnTree = null;
		Token token; 
		if (s == null || s.isEmpty()) 
			return null; 
		
		token = s.pop(); 
		// need to handle stack underflow 
		if ((token instanceof LiteralToken) || (token instanceof CellToken) ) { 
			// Literals and Cells are leaves in the expression tree 
			returnTree = new ExpressionTreeNode(token, null, null); 
		} 
		else if (token instanceof OperatorToken) { 
			// Continue finding tokens that will form the 
			// right subtree and left subtree. 
			ExpressionTreeNode rightSubtree = GetExpressionTree(s); 
			ExpressionTreeNode leftSubtree = GetExpressionTree(s); 
			returnTree = new ExpressionTreeNode(token, leftSubtree, rightSubtree); 
		} 
		
		return returnTree;
	}
	
	/**
	 * Build an expression tree from a stack of ExpressionTreeTokens 
	 * @param s stack of ExpressionTreeTokens 
	 */
	public void BuildExpressionTree (Stack <Token> s) { 
		root = GetExpressionTree(s); 
		
		if (!s.isEmpty()) { 
			System.out.println("Error in BuildExpressionTree."); 
		}
	}
	
	public boolean FindIfCellExists(Spreadsheet s, int uniqueCellId){
		return this.root.RecursiveFindCellById(s, uniqueCellId);
	}
}
