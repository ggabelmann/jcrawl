package jcrawl.handler.document;

import org.jsoup.nodes.Document;

/**
 * This interface is a Strategy that allows Classes to create urls from Documents.
 */
public interface DocumentSelectStrategy {
	
	/**
	 * @param document Cannot be null.
	 * @return An Iterable for urls. Will not be null.
	 */
	public Iterable<String> getUrls(final Document document);
	
}
