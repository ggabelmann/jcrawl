package jcrawl.delay;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An implementation of Window that uses an Lock to block Threads so that the desired rate-limit is maintained.
 */
public class BlockingWindow implements Window {

    private Window window;
    private final Lock lock;

    public BlockingWindow(final Window window) {
        this.window = window;
        this.lock = new ReentrantLock();
    }

    /**
     * Grabs a lock before adding the given event, waiting if necessary.
     * Does internal mutation and returns this object, not a new one.
     * I did this because I don't know what happens if threads are blocked on the lock and then a new object is returned.
     */
    @Override
    public Window addEvent(final long time) {
        lock.lock();
        try {
            for (;;) {
                final long delayAmount = calculateDelay();
                if (delayAmount <= 0) {
                    break;
                }
                else {
                    Thread.sleep(delayAmount);
                    System.out.println(String.format("# slept for %d ms", delayAmount));
                }
            }

            window = window.addEvent(time);
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }

        return this;
    }

    /**
     * Calculates the delay without blocking.
     */
    @Override
    public long calculateDelay() {
        return window.calculateDelay();
    }

}
