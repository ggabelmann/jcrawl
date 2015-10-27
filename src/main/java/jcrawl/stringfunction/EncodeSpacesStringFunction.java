package jcrawl.stringfunction;

import java.util.function.UnaryOperator;

public class EncodeSpacesStringFunction implements UnaryOperator<String> {
	
	/**
	 * @return A new String where ' ' before '?' is encoded as '%20', and ' ' after '?' is encoded as '+'.
	 */
	@Override
	public String apply(final String input) {
		final int index = input.indexOf("?");
		if (index < 0) {
			return input;
		}
		else {
			return input.substring(0, index).replace(" ", "%20") + input.substring(index, input.length()).replace(" ", "+");
		}
	}

}
