package jcrawl.stringfunction;

import jcrawl.AbstractRegex;
import jcrawl.stringfunction.StringFunction;

/**
 * ?
 */
public class LowerCaseHttpStringFunction extends AbstractRegex implements StringFunction {
	
	public LowerCaseHttpStringFunction() {
		super("http://.*");
	}
	
	/**
	 * If the given String starts with "http://" then it is lower-cased.
	 * Otherwise no change is done.
	 */
	@Override
	public String apply(final String input) {
		if (getMatcher(input).matches()) {
			return "http://" + input.substring(7);
		}
		else {
			return input;
		}
	}

}
