package jcrawl.stringfunction;

import java.util.function.UnaryOperator;

/**
 * This Class does a simple String.trim() on urls.
 */
public class TrimStringFunction implements UnaryOperator<String> {

	@Override
	public String apply(final String input) {
		return input.trim();
	}

}
