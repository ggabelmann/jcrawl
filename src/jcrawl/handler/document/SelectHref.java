package jcrawl.handler.document;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This Class selects "a href" from Documents.
 */
public class SelectHref implements SelectFromDocumentStrategy {

	@Override
	public Elements getElements(final Document document) {
		return document.select("a[href]");
	}

	@Override
	public String getHref(final Element element) {
		return element.attr("abs:href");
	}
	
}

