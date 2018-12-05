package jcrawl.core;

import java.util.Objects;

/**
 * This class is analagous to an URL, but simpler.
 * This is better than just using Strings everywhere because a link is not text.
 */
public class Link {

    private final String value;

    public Link(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(value, link.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

}
