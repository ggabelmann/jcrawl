package jcrawl.crawl;

import java.util.Arrays;
import jcrawl.ChainOfResponsibility;
import jcrawl.Regexes;
import jcrawl.Utils;
import jcrawl.handler.DiscardDuplicatesHandler;
import jcrawl.handler.DiscardHandler;
import jcrawl.handler.Handler;
import jcrawl.handler.HtmlHandler;
import jcrawl.handler.PrintHandler;
import jcrawl.handler.StringFunctionHandler;
import jcrawl.handler.document.Select;
import jcrawl.queue.ChainedComparator;
import jcrawl.queue.PriorityQueue;
import jcrawl.queue.Queue;
import jcrawl.queue.RegexComparator;
import jcrawl.stringfunction.EncodeSpacesStringFunction;
import jcrawl.stringfunction.ExtractStringFunction;
import jcrawl.stringfunction.LowerCaseHttpStringFunction;
import jcrawl.stringfunction.SubstringStringFunction;
import jcrawl.stringfunction.TrimStringFunction;

/**
 * Crawl the bootiemashup.com site and print out links to mp3s, pdfs, and zips.
 */
public class CrawlBootieMashup {

	private static final String[] PRINT_REGEXES =
		new String[] {
			Regexes.MP3,
			Regexes.PDF,
			Regexes.ZIP};
	
	private static final String[] DISCARD_REGEXES =
		new String[] {
			".+BlogBacklinkURL.+",	// Discard these links because they are broken.
			Regexes.GIF,
			Regexes.JPG,
			Regexes.PNG,
			Regexes.XML};	// Discard these links because they are mostly rss or atom documents.
	
	public static void main(String[] args) throws Exception {
		final Handler[] handlers = new Handler[] {
				new StringFunctionHandler(
					new TrimStringFunction(),
					new LowerCaseHttpStringFunction(),
					new SubstringStringFunction("#"),
					new SubstringStringFunction("?"),
					new EncodeSpacesStringFunction()),
				
				new StringFunctionHandler(new ExtractStringFunction("(.+)/feed.*")),	// The /feed part of the link is superfluous.
				
				new DiscardHandler(Utils.orRegexes(DISCARD_REGEXES), true),
				new DiscardDuplicatesHandler(),
				
				new PrintHandler(PRINT_REGEXES),
				new HtmlHandler(Utils.orRegexes("http://bootiemashup.com.*"), new Select("a", "href"))
		};
		
		final Queue queue = new PriorityQueue(
		   new ChainedComparator<>(
		      new RegexComparator(PRINT_REGEXES[2]),
		      new RegexComparator(PRINT_REGEXES[0]),
		      new RegexComparator(PRINT_REGEXES[1])));
		queue.add(Arrays.asList("http://bootiemashup.com/blog", "http://bootiemashup.com/bestof"));
		
		final ChainOfResponsibility chain = new ChainOfResponsibility(handlers, queue);
		chain.start();
	}
	
}
