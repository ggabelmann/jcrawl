package jcrawl.stringfunction;

import java.util.regex.Matcher;

import jcrawl.AbstractRegex;

/**
 * This Class uses a regex to extract parts of Strings and then piece them back together.
 */
public class ExtractStringFunction extends AbstractRegex implements StringFunction {
	
	public ExtractStringFunction(final String regex) {
		super(regex);
	}
	
	/**
	 * As an example, if the given regex was "(.+/)foo/(.+)" and the regex matches the given input then the returned String will have "foo/" removed.
	 * If the regex does not match, or it does match but there are no match groups (that is, no "()" in the regex) then the given input is returned.
	 */
	@Override
	public String apply(final String input) {
		final Matcher matcher = getMatcher(input);
		
		if (matcher.matches() && matcher.groupCount() >= 1) {
			final StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= matcher.groupCount(); i++) {
				sb.append(matcher.group(i)); 
			}
			return sb.toString();
		}
		return input;
	}
	
}
