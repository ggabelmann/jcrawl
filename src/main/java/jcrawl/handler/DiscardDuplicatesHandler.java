package jcrawl.handler;

import jcrawl.stringfunction.CopyStringFunction;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

/**
 * This Class can prevent infinite loops by adding urls to an internal Set.
 */
public class DiscardDuplicatesHandler implements Handler {
	
	private Function<String, String> function;
	private Set<String> visited;

	public DiscardDuplicatesHandler() {
		this(new CopyStringFunction());
	}
	
	/**
	 * Constructor.
	 * 
	 * @param f This StringFunction is called on the url before its internal Set is checked. Cannot be null.
	 */
	public DiscardDuplicatesHandler(final Function<String, String> f) {
		this.function = f;
		this.visited = new HashSet<>();
	}

	private Set<String> getVisited() {
		return visited;
	}

	/**
	 * If the url is not in the internal Set then null is returned.
	 * Otherwise an empty Iterator is returned.
	 */
	@Override
	public Optional<Iterable<String>> handle(final String url) {
		final String transformed = function.apply(url);

		if (getVisited().contains(transformed)) {
			// System.out.println("# DuplicateHandler: " + matched);
			return Optional.of(Collections.emptyList());
		}
		else {
			getVisited().add(transformed);
			return Optional.empty();
		}
	}

}
