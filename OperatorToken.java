package app;
/**
 * This class holds the operator characters for the purpose of making them a token
 * of type "OperatorToken"
 */
public class OperatorToken extends Token {
	/**
	 * The constant that is the + character.
	 */
	public static final char Plus = '+';
	
	/**
	 * The constant that is the - character.
	 */
	public static final char Minus = '-';
	
	/**
	 * The constant that is the multiply character.
	 */
	public static final char Mult = '*'; 
	
	/**
	 * The constant that is the division character.
	 */
	public static final char Div = '/'; 
	
	/**
	 * The constant that is the left parenthesis character.
	 */
	public static final char LeftParen = '('; 
	
	/**
	 * The field that holds the current operator token.
	 */
	private char operatorToken; 
	
	/**
	 * A method that sets operator char of this operator token.
	 * @param ch the operator char
	 */
	public OperatorToken(char ch){
		this.operatorToken = ch;
	}
	/**
	 * A method that returns this operator token.
	 * @return this operator token.
	 */
	public char getOperatorToken(){
		return operatorToken;
	}
	
	/**
	 * Return the priority of this OperatorToken.
	 *
	 * priorities:
	 *   +, - : 0
	 *   *, / : 1
	 *   (    : 2
	 *
	 * @return  the priority of operatorToken
	 */
	int priority () {
	    switch (this.operatorToken) {
	        case Plus:
	            return 0;
	        case Minus:
	            return 0;
	        case Mult:
	            return 1;
	        case Div:
	            return 1;
	        case LeftParen:
	            return 2;

	        default:
	            // This case should NEVER happen
	            System.out.println("Error in priority.");
	            System.exit(0);
	            break;
	    }
		return -1;
	}
}
