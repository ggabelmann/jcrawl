package jcrawl.queue;

import java.util.Comparator;

/**
 * This Class effectively randomizes the sorting of Strings by using their hashcodes.
 */
public class HashcodeComparator implements Comparator<String> {
   
   @Override
   public int compare(final String url0, final String url1) {
      final int hash0 = url0.hashCode();
      final int hash1 = url1.hashCode();
      return new Integer(hash0).compareTo(hash1);
   }
   
}
