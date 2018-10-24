package jcrawl.core;

import com.google.common.collect.ImmutableSet;

import java.util.*;
import java.util.function.Function;

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
     * First transforms the given links using the function supplied to the constructor.
     * Second, removes the duplicates by looking at its history.
     * Third, creates a new queue, adds the transformed links, and sorts it.
     */
    @Override
    public Queue add(final Set<Link> addLinks) {
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

    public ImmutableSet<Link> getHistory() {
        return history;
    }

}
