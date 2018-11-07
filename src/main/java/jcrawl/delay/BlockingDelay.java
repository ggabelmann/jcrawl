package jcrawl.delay;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An implementation of Delay that uses an Lock to block Threads so that the desired 'load' is maintained.
 */
public class BlockingDelay implements Delay {

    private Delay delay;
    private final Lock lock;

    public BlockingDelay(final Delay delay) {
        this.delay = delay;
        this.lock = new ReentrantLock();
    }

    /**
     * Grabs a lock before adding the given event, waiting if necessary.
     * Does internal mutation and returns itself, not a new object.
     */
    @Override
    public Delay addEvent(final long time) {
        lock.lock();
        try {
            for (;;) {
                final long delayAmount = calculateDelay();
                if (delayAmount == 0) {
                    break;
                }
                else {
                    Thread.sleep(delayAmount);
                    System.out.println(String.format("# slept for %d ms", delayAmount));
                }
            }

            delay = delay.addEvent(System.currentTimeMillis());
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
        return delay.calculateDelay();
    }

}
