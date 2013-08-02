package jcrawl;

import java.util.Iterator;

import jcrawl.handler.Handler;
import jcrawl.queue.Queue;

/**
 * This Class implements a chain of responsibility for a queue of urls (Strings, actually).
 */
public class ChainOfResponsibility {
	
	private final Handler[] handlers;
	private final Queue queue;
	
	/**
	 * Constructor.
	 * 
	 * @param handlers This array is copied to an internal array. Cannot be null.
	 * @param queue Not copied. (Should it be?) Cannot be null.
	 */
	public ChainOfResponsibility(final Handler[] handlers, final Queue queue) {
		this.handlers = new Handler[handlers.length];
		this.queue = queue;
		
		System.arraycopy(handlers, 0, this.handlers, 0, handlers.length);
	}
	
	private Queue getQueue() {
		return queue;
	}
	
	/**
	 * Starts processing the urls in the queue.
	 * If there is no Handler for an url then it is printed out as a comment.
	 */
	public void start() {
		String url = getQueue().remove();
		int count = 1;
		
		for (; url != null; count++) {
			try {
				Iterator<String> handled = null;
				for (final Handler handler : handlers) {
					handled = handler.handle(url);
					if (handled != null) {
						getQueue().add(handled);
						break;
					}
				}
				
				if (handled == null) {
					System.out.println("# No handler for: " + url);
				}
			}
			catch (final Exception ex1) {
				System.out.println("# " + ex1);
			}
			
			if (count == 2500) {
				System.out.println("# Queue size: " + getQueue().getSize());
				count = 0;
			}
			url = getQueue().remove();
		}
	}
	
}
