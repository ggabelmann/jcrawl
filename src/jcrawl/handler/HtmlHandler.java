package jcrawl.handler;

import jcrawl.AbstractRegex;
import jcrawl.Utils;
import jcrawl.handler.document.SelectFromDocumentStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This Class fetches urls and uses a Strategy to extract urls from them.
 */
public class HtmlHandler extends AbstractRegex implements Handler {
	
	private final SelectFromDocumentStrategy strategy;
	
	public HtmlHandler(final String regex, final SelectFromDocumentStrategy strategy) {
		super(regex);
		this.strategy = strategy;
	}
	
	private SelectFromDocumentStrategy getStrategy() {
		return strategy;
	}

	@Override
	public Iterator<String> handle(final String url) {
		if (getMatcher(url).matches()) {
			final Document document = Utils.fetchAsDocument(url);
			final Elements hrefs = getStrategy().getElements(document);
			final List<String> urls = new ArrayList<String>();

			for (final Element element : hrefs) {
				final String href = getStrategy().getHref(element);
				urls.add(href);
			}
			return urls.iterator();
		}
		else {
			return null;
		}
	}
	
}

