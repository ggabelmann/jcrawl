package jcrawl.delay;

/**
 * A class that uses the Composite design pattern to manage many Window objects.
 */
public class CompositeWindow implements Window {

    private final Window[] delays;

    public CompositeWindow(final Window... delays) {
        this.delays = delays;
    }

    @Override
    public Window addEvent(final long time) {
        final Window[] newDelays = new Window[delays.length];

        for (int i = 0; i < delays.length; i++) {
            newDelays[i] = delays[i].addEvent(time);
        }

        return new CompositeWindow(newDelays);
    }

    /**
     * @return The max delay of all child calculations.
     */
    @Override
    public long calculateDelay() {
        long max = 0;

        for (final Window di : delays) {
            max = Math.max(max, di.calculateDelay());
        }

        return max;
    }

}
