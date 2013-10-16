package jcrawl.crawl;

import jcrawl.ChainOfResponsibility;
import jcrawl.Regexes;
import jcrawl.Utils;
import jcrawl.handler.DiscardHandler;
import jcrawl.handler.DuplicateHandler;
import jcrawl.handler.Handler;
import jcrawl.handler.HtmlHandler;
import jcrawl.handler.PrintHandler;
import jcrawl.handler.StringFunctionHandler;
import jcrawl.handler.document.Select;
import jcrawl.queue.ChainedComparator;
import jcrawl.queue.PriorityQueue;
import jcrawl.queue.Queue;
import jcrawl.queue.RegexComparator;
import jcrawl.stringfunction.ExtractStringFunction;
import jcrawl.stringfunction.LowerCaseHttpStringFunction;
import jcrawl.stringfunction.SearchReplaceStringFunction;
import jcrawl.stringfunction.SubstringStringFunction;
import jcrawl.stringfunction.TrimStringFunction;

import com.google.common.collect.Iterators;

/**
 * Crawl the bootiemashup.com site and print out links to mp3s, pdfs, and zips.
 */
public class CrawlBootieMashup {

	private static final String[] REGEXS_PRINT = new String[] {Regexes.MP3, Regexes.PDF, Regexes.ZIP};

	public static void main(String[] args) throws Exception {
		final Handler[] handlers = new Handler[] {
				new StringFunctionHandler(new TrimStringFunction()),
				new StringFunctionHandler(new LowerCaseHttpStringFunction()),
				new StringFunctionHandler(new SearchReplaceStringFunction(" ", "+")),
				new StringFunctionHandler(new SubstringStringFunction("#")),
				new StringFunctionHandler(new SubstringStringFunction("?")),
				new StringFunctionHandler(new ExtractStringFunction("(.+)/feed.*")),	// The /feed part of the link is superfluous.
				
				new DiscardHandler(".+BlogBacklinkURL.+"),	// Discard these links because they are broken.
				new DiscardHandler(Regexes.GIF),
				new DiscardHandler(Regexes.JPG),
				new DiscardHandler(Regexes.PNG),
				new DiscardHandler(Regexes.XML),	// Discard these links because they are mostly rss or atom documents.
				
				new DuplicateHandler(),
				
				new PrintHandler(REGEXS_PRINT),
				
				new HtmlHandler(Utils.orRegexes("http://bootiemashup.com/blog.*", "http://bootiemashup.com/bestof.*"), new Select("a", "href")),
		};
		
		final Queue queue = new PriorityQueue(
		   new ChainedComparator<String>(
		      new RegexComparator(REGEXS_PRINT[2]),
		      new RegexComparator(REGEXS_PRINT[0]),
		      new RegexComparator(REGEXS_PRINT[1])));
		queue.add(Iterators.forArray(new String[] {"http://bootiemashup.com/blog", "http://bootiemashup.com/bestof"}));
		
		final ChainOfResponsibility chain = new ChainOfResponsibility(handlers, queue);
		chain.start();
	}
	
}
