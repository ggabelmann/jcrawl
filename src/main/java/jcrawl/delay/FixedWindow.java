package jcrawl.delay;

import java.time.Duration;

/**
 * A window that holds no events and returns a fixed delay.
 */
public class FixedWindow implements Window {

    private final Duration duration;

    public FixedWindow(final Duration duration) {
        this.duration = duration;
    }

    @Override
    public Window addEvent(long time) {
        return this;
    }

    @Override
    public long calculateDelay() {
        return duration.toMillis();
    }

}
