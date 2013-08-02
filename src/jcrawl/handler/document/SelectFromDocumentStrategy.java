package jcrawl.handler.document;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This interface is a Strategy that allows Classes to select Elements from Documents, and create Hrefs for Elements.
 */
public interface SelectFromDocumentStrategy {
	
	/**
	 * @param document Cannot be null.
	 * @return An Elements object. Will not be null.
	 */
	public Elements getElements(final Document document);
	
	/**
	 * @param element Cannt be null.
	 * @return An Href String. Will not be null.
	 */
	public String getHref(final Element element);
	
}
