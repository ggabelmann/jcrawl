package jcrawl.stringfunction;

import java.util.function.UnaryOperator;

/**
 * This Class does a simple search and replace on an url.
 */
public class SearchReplaceStringFunction implements UnaryOperator<String> {

	private final String search;
	private final String replace;
	
	/**
	 * Constructor.
	 * 
	 * @param search The String to search for. It is not a regex. Cannot be null.
	 * @param replace
	 */
	public SearchReplaceStringFunction(final String search, final String replace) {
		this.search = search;
		this.replace = replace;
	}
	
	/**
	 * Does a search/replace using String.replace().
	 */
	@Override
	public String apply(final String input) {
		return input.replace(search, replace);
	}

}
