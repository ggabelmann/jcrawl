package jcrawl.handler;

import java.util.Iterator;

/**
 * ?
 */
public interface Handler {
	
	/**
	 * If the given url was not handled then null must be returned.
	 * Otherwise, an Iterator must be returned with 0 or more urls inside it.
	 * 
	 * @param url Cannot be null.
	 * @return An Iterator or else null.
	 */
	public Iterator<String> handle(final String url);
	
}
