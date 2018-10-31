package jcrawl.fetch;

/**
 * A class that uses the Composite design pattern to manage many Delay objects.
 */
public class CompositeDelay implements Delay {

    private final Delay[] delays;

    public CompositeDelay(final Delay... delays) {
        this.delays = delays;
    }

    @Override
    public Delay addEvent(final long time) {
        final Delay[] newDelays = new Delay[delays.length];

        for (int i = 0; i < delays.length; i++) {
            newDelays[i] = delays[i].addEvent(time);
        }

        return new CompositeDelay(newDelays);
    }

    /**
     * @return The max delay of all child calculations.
     */
    @Override
    public long calculateDelay() {
        long max = 0;

        for (final Delay di : delays) {
            max = Math.max(max, di.calculateDelay());
        }

        return max;
    }

}
