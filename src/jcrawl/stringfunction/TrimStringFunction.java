package jcrawl.stringfunction;

import jcrawl.stringfunction.StringFunction;

/**
 * This Class does a simple String.trim() on urls.
 */
public class TrimStringFunction implements StringFunction {

	@Override
	public String apply(final String input) {
		return input.trim();
	}

}
