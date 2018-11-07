package jcrawl.delay;

import java.time.Duration;

public class FixedDelay implements Delay {

    private final Duration duration;

    public FixedDelay(final Duration duration) {
        this.duration = duration;
    }

    @Override
    public Delay addEvent(long time) {
        return this;
    }

    @Override
    public long calculateDelay() {
        return duration.toMillis();
    }

}
