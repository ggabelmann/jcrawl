package jcrawl.handler;

import jcrawl.AbstractRegex;

import java.util.Iterator;

import com.google.common.collect.Iterators;

/**
 * This Class keeps urls which match a regex.
 */
public class KeepHandler extends AbstractRegex implements Handler {
	
	public KeepHandler(final String regex) {
		super(regex);
	}
	
	@Override
	public Iterator<String> handle(final String url) {
		if (getMatcher(url).matches()) {
			return null;
		}
		else {
			// System.out.println("# KeepHandler: " + url);
			return Iterators.emptyIterator();
		}
	}
	
}
