package jcrawl.delay;

/**
 * An interface for objects that can record events and can calculate delays so that some rate-limit can be maintained.
 *
 * Note: This class assumes events have zero width.
 * They have no start and end time, no weight to them, etc.
 */
public interface Window {

    /**
     * @param time The time at which the event occurred.
     * @return A Window (perhaps new or perhaps this one) that has been updated.
     */
    Window addEvent(long time);

    /**
     * @return The number of milliseconds to delay.
     */
    long calculateDelay();

}
