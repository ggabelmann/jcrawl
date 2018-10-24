package jcrawl.transform;

import jcrawl.core.Link;

import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * This Class uses a regexp to find and replace all matches.
 */
public class ReplaceAll extends AbstractRegex implements Function<Set<Link>, Set<Link>> {

	private final String replaceWith;

    /**
     * @param regex The regexp.
     * @param replaceWith The literal String.
     */
	public ReplaceAll(final String regex, final String replaceWith) {
		super(regex);
		this.replaceWith = replaceWith;
	}
	
	@Override
	public Set<Link> apply(final Set<Link> links) {
        return links.stream()
                .map(this::helper)
                .collect(Collectors.toSet());
	}

	private Link helper(final Link link) {
		final Matcher matcher = getMatcherFor(link.getValue());
        return new Link(matcher.replaceAll(replaceWith));
	}

}
