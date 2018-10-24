package jcrawl.transform;

import jcrawl.core.Link;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This Class discards links which match a regex.
 */
public class Discard implements Function<Set<Link>, Set<Link>> {

	private final Predicate<Link> regex;
	private final boolean print;

	public Discard(final Predicate<Link> regex) {
		this(regex, false);
	}

	/**
	 * Constructor.
	 *
	 * @param regex Cannot be null.
	 * @param print If true then when a link is discarded it will be printed with a preceding #.
	 */
	public Discard(final Predicate<Link> regex, final boolean print) {
		this.regex = regex;
	    this.print = print;
	}
	
	private boolean isPrint() {
		return print;
	}

	@Override
	public Set<Link> apply(final Set<Link> links) {
		return links.stream()
				.filter(this::helper)
				.collect(Collectors.toSet());
	}

	public boolean helper(final Link link) {
		if (regex.test(link)) {
			if (isPrint()) {
				System.out.println(String.format("# DiscardHandler: %s", link));
			}
			return false;
		}
		else {
			return true;
		}
	}
}
