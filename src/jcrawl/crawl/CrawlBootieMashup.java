package jcrawl.crawl;

import jcrawl.ChainOfResponsibility;
import jcrawl.Regexes;
import jcrawl.handler.DiscardHandler;
import jcrawl.handler.DuplicateHandler;
import jcrawl.handler.Handler;
import jcrawl.handler.HtmlHandler;
import jcrawl.handler.KeepHandler;
import jcrawl.handler.PrintHandler;
import jcrawl.handler.StringFunctionHandler;
import jcrawl.handler.document.SelectHref;
import jcrawl.queue.PriorityQueue;
import jcrawl.queue.Queue;
import jcrawl.queue.RegexComparator;
import jcrawl.stringfunction.LowerCaseHttpStringFunction;
import jcrawl.stringfunction.SearchReplaceStringFunction;
import jcrawl.stringfunction.SubstringStringFunction;
import jcrawl.stringfunction.TrimStringFunction;

import com.google.common.collect.Iterators;

/**
 * Crawl the bootiemashup.com site and print out links to mp3s, pdfs, and zips.
 */
public class CrawlBootieMashup {

	public static void main(String[] args) throws Exception {
		final Handler[] handlers = new Handler[] {
				new StringFunctionHandler(new TrimStringFunction()),
				new StringFunctionHandler(new LowerCaseHttpStringFunction()),
				new StringFunctionHandler(new SearchReplaceStringFunction(" ", "+")),
				new StringFunctionHandler(new SubstringStringFunction("#")),
				
				new DiscardHandler(".+/feed"),				// Discard links which end with /feed because they are useless duplicates of good links.
				new DiscardHandler(".+BlogBacklinkURL.+"),	// Discard these links because they are broken.
				new DiscardHandler(Regexes.GIF),
				new DiscardHandler(Regexes.JPG),
				new DiscardHandler(Regexes.PNG),
				new DiscardHandler(Regexes.XML),
				
				new DuplicateHandler(),
				
				new PrintHandler(Regexes.MP3),
				new PrintHandler(Regexes.PDF),
				new PrintHandler(Regexes.ZIP),
				
				new KeepHandler("http://bootiemashup.com/blog.*|http://bootiemashup.com/bestof.*"),
				
				new HtmlHandler(".+", new SelectHref()),
		};
		
		final Queue queue = new PriorityQueue(new RegexComparator(new String[] {Regexes.MP3, Regexes.ZIP}));
		queue.add(Iterators.forArray(new String[] {"http://bootiemashup.com/blog", "http://bootiemashup.com/bestof"}));
		
		final ChainOfResponsibility chain = new ChainOfResponsibility(handlers, queue);
		chain.start();
	}
	
}
