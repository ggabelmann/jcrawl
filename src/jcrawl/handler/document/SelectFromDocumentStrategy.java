package jcrawl.handler.document;

import java.util.Iterator;

import org.jsoup.nodes.Document;

/**
 * This interface is a Strategy that allows Classes to create urls from Documents.
 */
public interface SelectFromDocumentStrategy {
	
	/**
	 * @param document Cannot be null.
	 * @return An Iterator for urls. Will not be null.
	 */
	public Iterator<String> getUrls(final Document document);
	
}
