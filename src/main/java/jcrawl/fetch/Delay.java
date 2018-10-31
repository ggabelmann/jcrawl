package jcrawl.fetch;

/**
 * An interface for immutable objects that can record events and can calculate delays so that the pace of events is good.
 *
 * Note: This class assumes events have zero width.
 * They have no start and end time, no weight to them, etc.
 */
public interface Delay {

    /**
     * @param time The time at which the event occured.
     * @return A new EventsWindow with updates (based on where the window is).
     */
    Delay addEvent(long time);

    /**
     *
     * @return The number of milliseconds to delay.
     */
    long calculateDelay();

}
