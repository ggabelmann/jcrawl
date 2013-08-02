package jcrawl.stringfunction;

import jcrawl.AbstractRegex;

/**
 * ?
 */
public class SubstringStringFunction extends AbstractRegex implements StringFunction {
	
	private final String substring;
	
	public SubstringStringFunction(final String substring) {
		this(".*", substring);
	}
	
	public SubstringStringFunction(final String regex, final String substring) {
		super(regex);
		this.substring = substring;
	}
	
	/**
	 * Performs a String.substring(0, index) on the given String (if it matches the regex).
	 * index is found by doing a String.indexOf().
	 */
	@Override
	public String apply(final String input) {
		if (getMatcher(input).matches()) {
			final int index = input.indexOf(substring);
			if (index >= 0) {
				return input.substring(0, index);
			}
		}
		return input;
	}

}
