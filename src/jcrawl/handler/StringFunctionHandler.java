package jcrawl.handler;

import jcrawl.stringfunction.StringFunction;

import java.util.Iterator;

import com.google.common.collect.Iterators;

/**
 * This Class wraps a StringFunction so that it can be used in a ChainOfResponsibility.
 */
public class StringFunctionHandler implements Handler {
	
	private final StringFunction function;
	
	public StringFunctionHandler(final StringFunction f) {
		this.function = f;
	}
	
	private StringFunction getFunction() {
		return function;
	}
	
	/**
	 * If the transformed url is not changed then null will be returned (that is, the url was not handled).
	 * Otherwise an Iterator is returned with the transformed url (so, it was handled).
	 */
	@Override
	public Iterator<String> handle(final String url) {
		final String transformed = getFunction().apply(url);
		
		if (transformed.equals(url)) {
			return null;
		}
		else {
			return Iterators.singletonIterator(transformed);
		}
	}

}
