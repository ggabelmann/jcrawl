package jcrawl.handler.document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This Class extracts "a href" urls from Documents.
 */
public class SelectHref implements SelectFromDocumentStrategy {

	@Override
	public Iterator<String> getUrls(final Document document) {
		final List<String> urls = new ArrayList<String>();
		final Elements hrefs = document.select("a[href]");

		for (final Element element : hrefs) {
			urls.add(element.attr("abs:href"));
		}
		return urls.iterator();
	}
	
}
