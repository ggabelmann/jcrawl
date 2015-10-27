package jcrawl.queue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * This Class sorts its urls according to the given Comparator.
 * Duplicate urls are removed.
 */
public class PriorityQueue implements Queue {
   
   private final NavigableSet<String> queue;
   
   public PriorityQueue(final Comparator<String> comparator) {
      this.queue = new TreeSet<String>(comparator);
   }
   
   @Override
   public void add(final Iterator<String> urls) {
      for (; urls.hasNext(); ) {
         queue.add(urls.next());
      }
   }
   
   @Override
   public String remove() {
      return queue.pollFirst();
   }
   
   @Override
   public int getSize() {
      return queue.size();
   }
   
}
