package jcrawl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Class is an abstract base for other Classes which need a regex and a Matcher.
 */
public abstract class AbstractRegex {

	private final Pattern pattern;
	
	public AbstractRegex(final String regex) {
		this.pattern = Pattern.compile(regex);
	}
	
	private Pattern getPattern() {
		return pattern;
	}
	
	/**
	 * ?
	 * 
	 * @param string
	 * @return A Matcher for the given String.
	 */
	protected Matcher getMatcher(final String string) {
		return getPattern().matcher(string);
	}
	
}
