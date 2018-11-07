package jcrawl.core;

import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Predicate;

/**
 * An interface for an immutable queue.
 *
 * Implementations may:
 * - Be sorted.
 * - Remove duplicate Links.
 * - Be a purely functional object ("value") that can be safely shared and stored.
 */
public interface Queue {

    /**
     * @return An updated queue.
     */
    Queue addAll(Set<Link> links);

    /**
     * Implementations may support advanced features of Spliterators.
     */
    Spliterator<Link> getSpliterator();

    /**
     * @return The next link, if there is one.
     */
    Optional<Link> peekHead();

    /**
     * Removes links from the queue that match the given Predicate.
     *
     * @return A queue without the matched links.
     */
    Queue remove(Predicate<Link> tester);

    /**
     * Note: an empty queue will return itself.
     *
     * @return A new queue that does not have the next link.
     */
    Queue removeHead();

}
