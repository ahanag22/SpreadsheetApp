package app;
/**
 * The class token that holds the literal integer value.
 */
public class LiteralToken extends Token {
	/**
	 * The variable that holds the integer.
	 */
	private int value;
	
	/**
	 * A method that sets the value of this literal token.
	 * @param value the value of this literal token.
	 */
	public LiteralToken(int value){
		this.value = value;
	}
    /**
     * A method that returns the value of this literal token in String.
     * @return the value of this literal token  in String.
     */
	public String getValue() {
		return Integer.toString(this.value);
	}
	/**
     * A method that returns the value of this literal token in integer.
     * @return the value of this literal token  in integer.
     */
	public int getIntValue() {
		return this.value;
	}
};
