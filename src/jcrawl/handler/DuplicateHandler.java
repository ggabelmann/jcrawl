package jcrawl.handler;

import jcrawl.stringfunction.CopyStringFunction;
import jcrawl.stringfunction.StringFunction;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Iterators;

/**
 * This Class can prevent infinite loops by adding urls to an internal Set.
 */
public class DuplicateHandler implements Handler {
	
	private StringFunction function;
	private Set<String> visited;

	public DuplicateHandler() {
		this(new CopyStringFunction());
	}
	
	/**
	 * Constructor.
	 * 
	 * @param f This StringFunction is called on the url before its internal Set is checked. Cannot be null.
	 */
	public DuplicateHandler(final StringFunction f) {
		this.function = f;
		this.visited = new HashSet<String>();
	}

	private StringFunction getFunction() {
		return function;
	}

	private Set<String> getVisited() {
		return visited;
	}

	/**
	 * If the url is not in the internal Set then null is returned.
	 * Otherwise an empty Iterator is returned.
	 */
	@Override
	public Iterator<String> handle(final String url) {
		final String transformed = getFunction().apply(url);

		if (getVisited().contains(transformed)) {
			// System.out.println("# DuplicateHandler: " + matched);
			return Iterators.emptyIterator();
		}
		else {
			getVisited().add(transformed);
			return null;
		}
	}

}
