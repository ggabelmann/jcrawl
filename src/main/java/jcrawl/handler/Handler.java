package jcrawl.handler;

import java.util.Optional;

public interface Handler {
	
	/**
	 * If the given url was handled then an Iterable must be returned with 0 or more urls inside it.
	 * Otherwise, an empty Optional must be returned.
	 * 
	 * @param url Cannot be null.
	 * @return The result.
	 */
	public Optional<Iterable<String>> handle(final String url);
	
}
