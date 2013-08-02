package jcrawl.stringfunction;

/**
 * ?
 */
public interface StringFunction {
	
	/**
	 * Applies a function to the given String.
	 * 
	 * @param input Cannot be null.
	 * @return The original String or a transformed String. Will not be null.
	 */
	public String apply(final String input);
	
}
