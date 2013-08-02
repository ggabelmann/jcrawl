package jcrawl.queue;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Class compares Strings using regular expressions.
 * Note that the regular expressions are not ordered.
 * If any regex matches then "they all match", so to speak. (They are "ORed" together.)
 * So this Class does not do "fine grained" sorting.
 */
public class RegexComparator implements Comparator<String> {
	
	private final Pattern[] patterns;
	
	public RegexComparator(final String[] regexs) {
		this.patterns = new Pattern[regexs.length];
		for (int i = 0; i < regexs.length; i++) {
			this.patterns[i] = Pattern.compile(regexs[i]);
		}
	}
	
	/**
	 * @return
	 * 0 if both urls match the regexes.
	 * 0 if neither urls match the regexes.
	 * -1 if the first url matches the regexes and the second url doesn't.
	 * 1 if the second url matches and the first url doesn't.
	 */
	@Override
	public int compare(final String url0, final String url1) {
		boolean url0MatchesAny = false;
		
		for (final Pattern pattern : patterns) {
			final Matcher matcher = pattern.matcher(url0);
			if (matcher.matches()) {
				url0MatchesAny = true;
				break;
			}
		}
		
		boolean url1MatchesAny = false;
		
		for (final Pattern pattern : patterns) {
			final Matcher matcher = pattern.matcher(url1);
			if (matcher.matches()) {
				url1MatchesAny = true;
				break;
			}
		}
		
		if (url0MatchesAny && url1MatchesAny) return 0;
		if (url0MatchesAny == false && url1MatchesAny == false) return 0;
		if (url0MatchesAny) return -1;
		return 1;
	}
	
}
