package jcrawl.fetch;

/**
 * Sleeps a thread to provide crude throttling.
 */
public class Delay implements Runnable {

    private final long ms;
    private long windowOpens;

    public Delay() {
        this(1000);
    }

    public Delay(final long intervalMilliSeconds) {
        this.ms = intervalMilliSeconds;
        this.windowOpens = System.currentTimeMillis();
    }

    public void run() {
        final long now = System.currentTimeMillis();
        final long delay = windowOpens - now + 1;

        if (delay >= 1) {
            try {
                Thread.sleep(delay);
                System.out.println(String.format("# slept for %d ms", delay));
                windowOpens += ms;
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            windowOpens = now + ms;
        }

        return;
    }

}
