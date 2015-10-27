package jcrawl.handler;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import jcrawl.AbstractRegex;
import jcrawl.Utils;
import jcrawl.handler.document.DocumentSelectStrategy;
import org.jsoup.nodes.Document;
import java.util.Optional;

/**
 * This Class fetches urls and uses a Strategy to extract urls from them.
 */
public class HtmlHandler extends AbstractRegex implements Handler {
	
	private final DocumentSelectStrategy[] strategies;
	
	public HtmlHandler(final String regex, final DocumentSelectStrategy... strategies) {
		super(regex);
		this.strategies = strategies;
	}
	
	private DocumentSelectStrategy[] getStrategy() {
		return strategies;
	}

	@Override
	public Optional<Iterable<String>> handle(final String url) {
		if (getMatcher(url).matches()) {
			final List<String> urls = new ArrayList<>();
			final Document document = Utils.fetchAsDocument(url);
			for (final DocumentSelectStrategy s : getStrategy()) {
				Iterables.addAll(urls, s.getUrls(document));
			}
			return Optional.of(urls);
		}
		else {
			return Optional.empty();
		}
	}
	
}

