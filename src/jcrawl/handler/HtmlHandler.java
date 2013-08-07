package jcrawl.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jcrawl.AbstractRegex;
import jcrawl.Utils;
import jcrawl.handler.document.SelectFromDocumentStrategy;

import org.jsoup.nodes.Document;

import com.google.common.collect.Iterators;

/**
 * This Class fetches urls and uses a Strategy to extract urls from them.
 */
public class HtmlHandler extends AbstractRegex implements Handler {
	
	private final SelectFromDocumentStrategy[] strategies;
	
	public HtmlHandler(final String regex, final SelectFromDocumentStrategy... strategies) {
		super(regex);
		this.strategies = strategies;
	}
	
	private SelectFromDocumentStrategy[] getStrategy() {
		return strategies;
	}

	@Override
	public Iterator<String> handle(final String url) {
		if (getMatcher(url).matches()) {
			final List<Iterator<String>> iterators = new ArrayList<Iterator<String>>();
			final Document document = Utils.fetchAsDocument(url);
			for (final SelectFromDocumentStrategy s : getStrategy()) {
				iterators.add(s.getUrls(document));
			}
			return Iterators.concat(iterators.iterator());
		}
		else {
			return null;
		}
	}
	
}

