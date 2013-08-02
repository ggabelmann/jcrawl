package jcrawl.stringfunction;

/**
 * This Class does a simple search and replace on an url.
 */
public class SearchReplaceStringFunction implements StringFunction {

	private final String search;
	private final String replace;
	
	public SearchReplaceStringFunction(final String search, final String replace) {
		this.search = search;
		this.replace = replace;
	}
	
	@Override
	public String apply(final String input) {
		return input.replace(search, replace);
	}

}
