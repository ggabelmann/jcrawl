package jcrawl.transform;

import jcrawl.core.Link;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This Class does a simple substring() on links.
 */
public class Substring implements Function<Set<Link>, Set<Link>> {

    private final Predicate<Link> regexp;
	private final String substring;

	public Substring(final String substring) {
		this((link) -> true, substring);
	}

	/**
	 * Only perform the substring() if the given regexp matches the link.
	 */
	public Substring(final Predicate<Link> regexp, final String substring) {
		this.regexp = regexp;
		this.substring = substring;
	}
	
	@Override
	public Set<Link> apply(final Set<Link> strings) {
        return strings.stream()
                .map(this::helper)
                .collect(Collectors.toSet());
	}

	private Link helper(final Link link) {
		if (regexp.test(link)) {
            final String input = link.getValue();
			final int index = input.indexOf(substring);
			if (index >= 0) {
				return new Link(input.substring(0, index));
			}
		}

		return link;
	}

}
