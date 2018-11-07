package jcrawl.core;

import com.google.common.collect.ImmutableSet;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An immutable queue that supports:
 * - A Function to transform links as they are added.
 * - A Comparator to sort links.
 * - A internal set (a history) to ensure than links are not added more than once.
 */
public class PriorityTransformingQueue implements Queue {

    private final Comparator<Link> comparator;
    private final ImmutableSet<Link> history;
    private final Link[] sorted;
    private final Function<Set<Link>, Set<Link>> transform;

    /**
     * Constructor.
     *
     * Note, the function that transform the links can do many transformations, but usually fall into two main groups:
     * - Canonicalize the links.
     * - Drop links ASAP so that they don't take up space in the history and definitely don't get fetched (which is very expensive).
     *
     * @param comparator A sorter for the links.
     * @param transform Ideally, pure functions without side-effects.
     */
    public PriorityTransformingQueue(final Comparator<Link> comparator, final Function<Set<Link>, Set<Link>> transform) {
        this(comparator, ImmutableSet.<Link>builder().build(), new Link[0], transform);
    }

    private PriorityTransformingQueue(final Comparator<Link> comparator, final ImmutableSet<Link> history, final Link[] sorted, final Function<Set<Link>, Set<Link>> transform) {
        this.comparator = comparator;
        this.history = history;
        this.sorted = sorted;
        this.transform = transform;
    }

    /**
     * First, transforms the given links using the function supplied to the constructor.
     * Second, removes the duplicates by looking at its history.
     * Third, creates a new queue, adds the transformed links, and sorts it.
     */
    @Override
    public Queue addAll(final Set<Link> addLinks) {
        final Set<Link> transformed = transform.apply(addLinks);
        transformed.removeAll(history);

        if (transformed.size() == 0) {
            return this;
        }
        else {
            final Link[] newLinks = new Link[transformed.size() + this.sorted.length];
            int i = 0;
            for (final Link s : transformed) {
                newLinks[i] = s;
                i++;
            }
            System.arraycopy(this.sorted, 0, newLinks, i, this.sorted.length);
            Arrays.sort(newLinks, comparator);

            final ImmutableSet.Builder<Link> newPrevious = ImmutableSet.<Link>builder().addAll(history);
            newPrevious.addAll(transformed);

            return new PriorityTransformingQueue(comparator, newPrevious.build(), newLinks, transform);
        }
    }

    @Override
    public Optional<Link> peekHead() {
        if (sorted.length == 0) {
            return Optional.empty();
        }
        else {
            return Optional.of(sorted[0]);
        }
    }

    @Override
    public Queue removeHead() {
        if (sorted.length == 0) {
            return this;
        }
        else if (sorted.length == 1) {
            return new PriorityTransformingQueue(comparator, history, new Link[0], transform);
        }
        else {
            final Link[] shortened = new Link[sorted.length - 1];
            System.arraycopy(sorted, 1, shortened, 0, shortened.length);
            return new PriorityTransformingQueue(comparator, history, shortened, transform);
        }
    }

    @Override
    public Queue remove(final Predicate<Link> tester) {
        int count = 0;
        final boolean[] keep = new boolean[sorted.length];

        for (int i = 0; i < sorted.length; i++) {
            if (tester.negate().test(sorted[i])) {
                keep[i] = true;
                count++;
            }
        }

        final Link[] newSorted = new Link[count];
        count = 0;

        for (int i = 0; i < keep.length; i++) {
            if (keep[i]) {
                newSorted[count] = sorted[i];
                count++;
            }
        }

        return new PriorityTransformingQueue(comparator, history, newSorted, transform);
    }

    public ImmutableSet<Link> getHistory() {
        return history;
    }

    public Spliterator<Link> getSpliterator() {
        return new InternalSpliterator();
    }


    //


    /**
     * Not threadsafe.
     * The Stream Framework guarantees that Spliterators won't be called by more than one thread at a time.
     */
    private class InternalSpliterator implements Spliterator<Link> {

        private int exclusiveEnd = sorted.length;
        private int next = 0;

        @Override
        public boolean tryAdvance(final Consumer<? super Link> action) {
            if (estimateSize() <= 0) {
                return false;
            }
            else {
                action.accept(sorted[next]);
                next++;
                return true;
            }
        }

        /**
         * If there is more than one Link remaining then a new Spliterator is returned that will only handle the next Link.
         * The remaining Links will be handled by this Spliterator.
         */
        @Override
        public Spliterator<Link> trySplit() {
            if (estimateSize() <= 1) {
                return null;
            }

            final InternalSpliterator internal = new InternalSpliterator();
            internal.next = next;
            internal.exclusiveEnd = next + 1;
            next++;
            return internal;
        }

        @Override
        public long estimateSize() {
            return exclusiveEnd - next;
        }

        @Override
        public int characteristics() {
            // Not SORTED on purpose because the Stream code wants to grab the Comparator.
            return  Spliterator.DISTINCT |
                    Spliterator.IMMUTABLE |
                    Spliterator.NONNULL |
                    Spliterator.ORDERED |
                    Spliterator.SIZED |
                    Spliterator.SUBSIZED;
        }
    }

}
