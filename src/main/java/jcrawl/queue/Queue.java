package jcrawl.queue;

/**
 * ?
 */
public interface Queue {
	
	/**
	 * @param urls Cannot be null.
	 */
	public void add(final Iterable<String> urls);
	
	/**
	 * @return The next url or null if the queue is empty.
	 */
	public String remove();
	
	/**
	 * @return The size of the queue.
	 */
	public int getSize();
	
}
