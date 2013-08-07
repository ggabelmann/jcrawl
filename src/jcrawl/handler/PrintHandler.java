package jcrawl.handler;

import jcrawl.AbstractRegex;
import jcrawl.Utils;

import java.util.Iterator;

import com.google.common.collect.Iterators;

/**
 * This Class prints urls which match a regex.
 */
public class PrintHandler extends AbstractRegex implements Handler {
	
	public PrintHandler(final String... regexes) {
		super(Utils.orRegexes(regexes));
	}
	
	@Override
	public Iterator<String> handle(final String url) {
		if (getMatcher(url).matches()) {
			System.out.println(url);
			return Iterators.emptyIterator();
		}
		else {
			return null;
		}
	}

}

