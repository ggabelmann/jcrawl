package jcrawl.stringfunction;

import java.util.function.UnaryOperator;

/**
 * ?
 */
public interface StringFunction extends UnaryOperator<String> {
	
	/**
	 * Applies a function to the given String.
	 * 
	 * @param input Cannot be null.
	 * @return The original String or a transformed String. Will not be null.
	 */
	@Override
	public String apply(final String input);
	
}
