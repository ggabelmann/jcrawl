package jcrawl.fetch;

import java.util.*;
import java.util.stream.LongStream;

/**
 * This class can record events and manage a sliding window over those events.
 * It can return estimates of how often work should be done to keep the window full.
 */
public class EventsWindow implements Delay {

    private final WindowDetails windowDetails;
    private final long[] events;

    /**
     * Constructor.
     *
     * @param count For example, 10 events.
     * @param ms For example, 10000 milliseconds.
     */
    public EventsWindow(final int count, final int ms) {
        this(new WindowDetails(count, ms), new long[0]);
    }

    private EventsWindow(final WindowDetails windowDetails, final long[] events) {
        this.windowDetails = windowDetails;
        this.events = events;
    }

    /**
     * Note: not optimized.
     */
    @Override
    public EventsWindow addEvent(final long time) {
        if (events.length == 0) {
            return new EventsWindow(windowDetails, new long[] {time});
        }

        final long[] newEvents = new long[events.length + 1];
        System.arraycopy(events, 0, newEvents, 0, events.length);
        newEvents[newEvents.length - 1] = time;
        Arrays.sort(newEvents);

        final int inWindow = inWindow(newEvents, newEvents[newEvents.length - 1]);

        final long[] truncated = new long[inWindow];
        System.arraycopy(newEvents, newEvents.length - truncated.length, truncated, 0, truncated.length);
        return new EventsWindow(windowDetails, truncated);
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
            if (array[i] <= windowEnd && array[i] >= windowEnd - windowDetails.getMs()) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * Note: not optimized.
     */
    public EventsWindow merge(final EventsWindow other) {
        EventsWindow next = this;

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
        final long futureTime = events[index] + getWindowDetails().getMs() + 1;
        return futureTime - now;
    }


    //


    public static class WindowDetails {

        private final int count;
        private final int ms;
        private final int period;

        WindowDetails(final int count, final int ms) {
            this.count = count;
            this.ms = ms;
            this.period = ms / count;
        }

        public int getCount() {
            return count;
        }

        public int getMs() {
            return ms;
        }

        public int getPeriod() {
            return period;
        }

    }

}
