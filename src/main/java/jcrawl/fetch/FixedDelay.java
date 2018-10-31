package jcrawl.fetch;

public class FixedDelay implements Delay {

    private final int ms;

    public FixedDelay(final int ms) {
        this.ms = ms;
    }

    @Override
    public Delay addEvent(long time) {
        return this;
    }

    @Override
    public long calculateDelay() {
        return ms;
    }

}
