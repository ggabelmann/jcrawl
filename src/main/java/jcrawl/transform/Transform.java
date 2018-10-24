package jcrawl.transform;

import jcrawl.core.Link;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * A class that is able to either pull out pieces of links or transform pieces of links using functions.
 */
public class Transform extends AbstractRegex implements Function<Set<Link>, Set<Link>> {

	private final Optional<List<Function<String, String>>> transforms;

    /**
     * @param regex The regexp to match the entire link.
     * @param transforms Must be Optional.empty() or have the same number of functions as there are "matching groups" in the regexp.
     */
	public Transform(final String regex, final Optional<List<Function<String, String>>> transforms) {
		super(regex);
		this.transforms = transforms;
	}
	
	@Override
	public Set<Link> apply(final Set<Link> links) {
        return links.stream()
                .map(this::helper)
                .collect(Collectors.toSet());
	}

	private Link helper(final Link link) {
		final Matcher matcher = getMatcherFor(link.getValue());

		if (matcher.matches() && matcher.groupCount() > 0) {
			final StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= matcher.groupCount(); i++) {
				if (transforms.isPresent()) {
                    sb.append(transforms.get().get(i - 1).apply(matcher.group(i)));
				}
				else {
                    sb.append(matcher.group(i));
				}
			}
			return new Link(sb.toString());
		}
		else {
			return link;
		}
	}

}
