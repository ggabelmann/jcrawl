package jcrawl.transform;

import jcrawl.core.Link;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This Class does a simple String.trim() on links.
 */
public class Trim implements Function<Set<Link>, Set<Link>> {

	@Override
	public Set<Link> apply(final Set<Link> strings) {
		return strings.stream()
				.map(Link::getValue)
				.map(String::trim)
				.map(Link::new)
				.collect(Collectors.toSet());
	}

}
