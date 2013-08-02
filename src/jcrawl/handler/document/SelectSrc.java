package jcrawl.handler.document;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This Class selects "img src" from Documents.
 */
public class SelectSrc implements SelectFromDocumentStrategy {
	
	@Override
	public Elements getElements(final Document document) {
		return document.select("img[src]");
	}

	@Override
	public String getHref(final Element element) {
		return element.attr("abs:src");
	}
	
}

