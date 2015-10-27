package jcrawl.queue;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Class compares Strings using regular expressions.
 */
public class RegexComparator implements Comparator<String> {
   
   private final Pattern pattern;
   
   /**
    * Constructor.
    * 
    * @param regex Cannot be null.
    */
   public RegexComparator(final String regex) {
      this.pattern = Pattern.compile(regex);
   }
   
   /**
    * @return
    * 0 if both urls match the regex.
    * 0 if neither urls match the regex.
    * -1 if the first url matches the regex and the second url doesn't.
    * 1 if the first url doesn't and the second url does.
    */
   @Override
   public int compare(final String url0, final String url1) {
      final Matcher matcher0 = pattern.matcher(url0);
      boolean url0Matches = matcher0.matches();
      
      final Matcher matcher1 = pattern.matcher(url1);
      boolean url1Matches = matcher1.matches();
      
      if ((url0Matches && url1Matches) || (url0Matches == false && url1Matches == false)) {
			return url0.compareTo(url1);
		}
      if (url0Matches) return -1;
      return 1;
   }
   
}
