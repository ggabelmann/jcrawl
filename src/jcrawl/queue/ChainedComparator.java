package jcrawl.queue;

import java.util.Comparator;

/**
 * This Class allows an array of Comparators to be used to sort objects.
 */
public class ChainedComparator<T> implements Comparator<T> {
	
   private final Comparator<T>[] comparators;
   
   /**
    * Constructor.
    * 
    * @param comparators May be empty. Cannot be null.
    */
	@SafeVarargs
   public ChainedComparator(final Comparator<T>... comparators) {
	   this.comparators = comparators;
	}
	
	/**
	 * If the array of comparators is empty then the return value is 0.
	 * Otherwise the first comparator is executed.
	 * If the result is not 0 then it is returned.
	 * Otherwise the next comparator is tried.
	 * And so on.
	 */
   @Override
   public int compare(final T o1, final T o2) {
      int result = 0;
      for (final Comparator<T> c : comparators) {
         result = c.compare(o1, o2);
         if (result != 0) {
            return result;
         }
      }
      return result;
   }
	
}
