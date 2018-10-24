package jcrawl.transform;

import jcrawl.core.Link;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This Class does a simple search and replace on a link.
 */
public class SearchReplace implements Function<Set<Link>, Set<Link>> {

	private final String search;
	private final String replace;

	/**
	 * Constructor.
	 *
	 * @param search The String to search for. It is not a regex. Cannot be null.
	 * @param replaceWith The literal String.
	 */
	public SearchReplace(final String search, final String replaceWith) {
		this.search = search;
		this.replace = replaceWith;
	}
	
	@Override
	public Set<Link> apply(final Set<Link> links) {
		return links.stream()
				.map(link -> link.getValue().replace(search, replace))
				.map(Link::new)
				.collect(Collectors.toSet());
	}

}
