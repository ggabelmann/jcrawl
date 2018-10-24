package jcrawl.core;

import java.util.Optional;
import java.util.Set;

/**
 * An interface for an immutable queue.
 * Ideally, the implementation is a purely functional object ("value") that can be safely shared and stored.
 */
public interface Queue {

    /**
     * @param links The new links.
     * @return An updated queue.
     */
    Queue add(Set<Link> links);

    /**
     * @return The next link, if there is one.
     */
    Optional<Link> peekHead();

    /**
     * Note: an empty queue will return itself.
     *
     * @return A new queue that does not have the next link.
     */
    Queue removeHead();

}
