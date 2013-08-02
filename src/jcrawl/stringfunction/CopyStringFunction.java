package jcrawl.stringfunction;

/**
 * This Class returns an exact copy of urls.
 */
public class CopyStringFunction implements StringFunction {
	
	/**
	 * @return An exact copy of the given String.
	 */
	@Override
	public String apply(final String input) {
		return input;
	}

}
