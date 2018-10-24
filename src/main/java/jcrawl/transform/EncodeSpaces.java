package jcrawl.transform;

import jcrawl.core.Link;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class encodes links such that each space before '?' is encoded as '%20', and each space after '?' is encoded as '+'.
 */
public class EncodeSpaces implements Function<Set<Link>, Set<Link>> {

    @Override
    public Set<Link> apply(final Set<Link> strings) {
        return strings.stream()
                .map(this::helper)
                .collect(Collectors.toSet());
    }

    private Link helper(final Link link) {
        final String input = link.getValue();
        final int index = input.indexOf("?");
        if (index < 0) {
            return new Link(input.replace(" ", "%20"));
        }
        else {
            return new Link(input.substring(0, index).replace(" ", "%20") + input.substring(index, input.length()).replace(" ", "+"));
        }
    }

}
