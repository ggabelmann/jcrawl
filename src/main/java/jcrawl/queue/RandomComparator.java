package jcrawl.queue;

import java.util.Comparator;
import java.util.Random;

/**
 * This Class effectively randomizes the sorting of Strings by using their hashcodes.
 */
public class RandomComparator implements Comparator<String> {
   
	private final Random random = new Random();
	
   @Override
   public int compare(final String url0, final String url1) {
		final int val = random.nextInt(3);
		return val - 1;
   }
   
}
