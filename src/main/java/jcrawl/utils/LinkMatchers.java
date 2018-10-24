package jcrawl.utils;

import jcrawl.core.Link;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * This Class contains static methods to create Predicates that can be used to match Links.
 */
public class LinkMatchers {

    public static Predicate<Link> of(final String regexp) {
        return new LinkMatcher(regexp);
    }

    public static Predicate<Link> or(final String... regexps) {
        if (regexps == null) {
            throw new IllegalArgumentException();
        }
        else if (regexps.length == 1) {
            return of(regexps[0]);
        }
        else {
            Predicate<Link> result = of(regexps[0]);
            for (int i = 1; i < regexps.length; i++) {
                result = result.or(of(regexps[i]));
            }
            return result;
        }
    }

    private static class LinkMatcher implements Predicate<Link> {

        private final Pattern pattern;

        private LinkMatcher(final String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

        @Override
        public boolean test(final Link link) {
            return pattern.matcher(link.getValue()).matches();
        }

    }

}
