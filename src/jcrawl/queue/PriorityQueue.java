package jcrawl.queue;

import java.util.Comparator;
import java.util.Iterator;

/**
 * This Class sorts its urls according to the given Comparator.
 */
public class PriorityQueue implements Queue {
	
	private final java.util.PriorityQueue<String> queue;
	
	public PriorityQueue(final Comparator<String> comparator) {
		this.queue = new java.util.PriorityQueue<String>(1024, comparator);
	}
	
	@Override
	public void add(final Iterator<String> urls) {
		for (; urls.hasNext(); ) {
			queue.add(urls.next());
		}
	}
	
	@Override
	public String remove() {
		return queue.poll();
	}
	
	@Override
	public int getSize() {
		return queue.size();
	}
	
}
