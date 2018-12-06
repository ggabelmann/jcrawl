package jcrawl.delay;

import java.time.Duration;
import java.util.*;
import java.util.stream.LongStream;

/**
 * This class can record events and manage a sliding window over those events.
 * It can return estimates of when the next event should occur to keep the window full.
 */
public class SlidingWindow implements Window {

    private final WindowDetails windowDetails;
    private final long[] events;

    /**
     * Constructor.
     *
     * @param count For example, 10 events.
     * @param window For example, 10 seconds.
     */
    public SlidingWindow(final int count, final Duration window) {
        this(new WindowDetails(count, window), new long[0]);
    }

    private SlidingWindow(final WindowDetails windowDetails, final long[] events) {
        this.windowDetails = windowDetails;
        this.events = events;
    }

    /**
     * First, inserts the given event into its sorted position.
     * Second, truncates the events which are older than the window.
     * This does not use the current time at all, only the events as they are.
     *
     * This does not block.
     *
     * Note: not optimized.
     */
    @Override
    public SlidingWindow addEvent(final long time) {
        if (events.length == 0) {
            return new SlidingWindow(windowDetails, new long[] {time});
        }

        final long[] newEvents = new long[events.length + 1];
        System.arraycopy(events, 0, newEvents, 0, events.length);
        newEvents[newEvents.length - 1] = time;
        Arrays.sort(newEvents);

        final int inWindow = inWindow(newEvents, newEvents[newEvents.length - 1]);

        final long[] truncated = new long[inWindow];
        System.arraycopy(newEvents, newEvents.length - truncated.length, truncated, 0, truncated.length);
        return new SlidingWindow(windowDetails, truncated);
    }

    /**
     * @param windowEnd Where the windows ends, in milliseconds.
     * @return The number of events in the window.
     */
    public int countEvents(final long windowEnd) {
        return inWindow(events, windowEnd);
    }

    public LongStream events() {
        return Arrays.stream(events);
    }

    public WindowDetails getWindowDetails() {
        return windowDetails;
    }

    private int inWindow(final long[] array, final long windowEnd) {
        int counter = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i] <= windowEnd && array[i] >= windowEnd - windowDetails.getWindow().toMillis()) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * Note: not optimized.
     */
    public SlidingWindow merge(final SlidingWindow other) {
        SlidingWindow next = this;

        for (final long event : other.events) {
            next = next.addEvent(event);
        }

        return next;
    }


    @Override
    public long calculateDelay() {
        /*
        Some documentation:
        Let's assume the desired number of events is 10 per 10 seconds.
        Find the number of events in the past 10 seconds (ie, relative to now).
         */
        final long now = System.currentTimeMillis();
        final int inWindow = inWindow(events, now);

        // If there are less than 10 events then we shouldn't delay at all.
        if (inWindow < getWindowDetails().count) {
            return 0;
        }

        /*
        We have 10 or more events, so we should delay some amount of time.
        Find the 10th event because once the window slides past it there will only be 9 events left.
         */
        final int index = events.length - getWindowDetails().getCount();

        /*
        The window will slide past the 10th event in 10001 ms.
        Therefor, we should delay for (that futureTime - now) ms.
         */
        final long futureTime = events[index] + getWindowDetails().getWindow().toMillis() + 1;
        final long result = futureTime - now;
        return result;
    }


    //


    public static class WindowDetails {

        private final int count;
        private final Duration window;
        private final Duration period;

        WindowDetails(final int count, final Duration window) {
            this.count = count;
            this.window = window;
            this.period = window.dividedBy(count);
        }

        public int getCount() {
            return count;
        }

        public Duration getWindow() {
            return window;
        }

        public Duration getPeriod() {
            return period;
        }

    }

}
