package jcrawl.crawl;

import jcrawl.ChainOfResponsibility;
import jcrawl.Regexes;
import jcrawl.handler.DuplicateHandler;
import jcrawl.handler.Handler;
import jcrawl.handler.HtmlHandler;
import jcrawl.handler.KeepHandler;
import jcrawl.handler.PrintHandler;
import jcrawl.handler.StringFunctionHandler;
import jcrawl.handler.document.Select;
import jcrawl.queue.ChainedComparator;
import jcrawl.queue.HashcodeComparator;
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
 * Crawl a specific blog on tumblr.com and print out links to jpgs.
 */
public class CrawlTumblr {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
         System.out.println("Usage: java jcrawl.crawl.CrawlTumblr url");
         System.out.println("eg:    java jcrawl.crawl.CrawlTumblr http://abc.tumblr.com");
		   System.exit(0);
		}
	   
		final String url = args[0];
		
		final Handler[] handlers = new Handler[] {
				new StringFunctionHandler(new TrimStringFunction()),
				new StringFunctionHandler(new LowerCaseHttpStringFunction()),
				new StringFunctionHandler(new SearchReplaceStringFunction(" ", "+")),
            new StringFunctionHandler(new SubstringStringFunction("?")),
            new StringFunctionHandler(new SubstringStringFunction("#")),
				
            new KeepHandler(".+media.tumblr.com.+", url + ".+"),
            
            new StringFunctionHandler(new SearchReplaceStringFunction("/post/", "/image/")),
            new StringFunctionHandler(new ExtractStringFunction("(.+/image/\\d+)/.+")), // Remove unnecessary SEO words from end of image link.
            
				new DuplicateHandler(),
				
				new PrintHandler(Regexes.JPG),
				
            new HtmlHandler(url + "/page/\\d+", new Select("a", "href")),
            new HtmlHandler(url + "/image/\\d+", new Select("img", "data-src")),
		};
		
      final Queue queue = new PriorityQueue(
            new ChainedComparator<String>(
                  new RegexComparator(Regexes.JPG),
                  new RegexComparator(".+/image/.+"),
                  new RegexComparator(".+/post/.+"),
                  new HashcodeComparator()
                  ));
      queue.add(Iterators.forArray(new String[] {url + "/page/1"}));
		
		final ChainOfResponsibility chain = new ChainOfResponsibility(handlers, queue);
		chain.start();
	}
	
}
