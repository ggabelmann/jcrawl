package jcrawl.core;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A simple, stateful, class that implements the core algorithm of a crawler.
 *
 * It has three main parts:
 * - An immutable queue that it pulls from, and then switches to a new queue with updated links.
 * - A consumer that may do things by side-effect, like printing, save to a DB, etc.
 * - A function that usually does a network call to find more links.
 *
 * This class does not support multi-threading because I don't want to overwhelm the target site.
 */
public class Crawler {

    private Queue q;
    private final Consumer<Link> consumer;
    private final Function<Link, Set<Link>> fetch;

    /**
     * @param q A queue that drives the crawler.
     * @param c A consumer that can choose to do something with a link.
     * @param f A function that gets new links and, unlike normal functions, may have side-effects because it usually does I/O.
     */
    public Crawler(final Queue q, final Consumer<Link> c, final Function<Link, Set<Link>> f) {
        this.q = q;
        this.consumer = c;
        this.fetch = f;
    }

    /**
     * @return A possibly new, updated, queue.
     */
    public Queue getQueue() {
        return q;
    }

    /**
     * Gets the next link off the queue and calls the consumer and the fetcher.
     * A new queue will be created and stored (because Queues are immutable).
     *
     * @return An optional ThrowableDetails that records info about an exception that may have occurred.
     */
    public Optional<ThrowableDetails> step() {
        final Optional<Link> optional = q.peekHead();
        if (optional.isPresent()) {
            final Link link = optional.get();
            q = q.removeHead();

            try {
                consumer.accept(link);
                final Set<Link> newLinks = fetch.apply(link);
                q = q.add(newLinks);
            }
            catch (final RuntimeException e) {
                return Optional.of(new ThrowableDetails(link, e));
            }
        }

        return Optional.empty();
    }


    //


    public static class ThrowableDetails {

        private final Link s;
        private final Throwable cause;

        public ThrowableDetails(final Link s, final Throwable cause) {
            this.s = s;
            this.cause = cause;
        }

        public Link getLink() {
            return s;
        }

        public Throwable getCause() {
            return cause;
        }

    }

}
