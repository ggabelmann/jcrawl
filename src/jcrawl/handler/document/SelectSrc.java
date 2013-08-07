package jcrawl.handler.document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This Class extracts "img src" urls from Documents.
 */
public class SelectSrc implements SelectFromDocumentStrategy {
	
	@Override
	public Iterator<String> getUrls(final Document document) {
		final List<String> urls = new ArrayList<String>();
		final Elements hrefs = document.select("img[src]");

		for (final Element element : hrefs) {
			urls.add(element.attr("abs:src"));
		}
		return urls.iterator();
	}
	
}
