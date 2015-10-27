package jcrawl.crawl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jcrawl.ChainOfResponsibility;
import jcrawl.Regexes;
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
import jcrawl.stringfunction.SearchReplaceStringFunction;
import jcrawl.stringfunction.SubstringStringFunction;
import jcrawl.stringfunction.TrimStringFunction;

/**
 * For interested photographers Victoria's Secret is a good source of glamour photos.
 */
public class CrawlVictoriasSecret {

	public static void main(String[] args) throws Exception {
		final Handler[] handlers = new Handler[] {
				new StringFunctionHandler(
					new TrimStringFunction(),
					new LowerCaseHttpStringFunction(),
					new SubstringStringFunction("#"),
					new EncodeSpacesStringFunction()),
				
				new DiscardHandler(".+[Cc]ustomer[Ss]ervice.*"),
				new DiscardHandler(".+/beauty.*"),
				new DiscardHandler(".+/imagefeatures.*"),
				new DiscardHandler(".+/moreInfo.*"),
				new DiscardHandler(".+/product/swatch.*"),
				new DiscardHandler(".+/resources.*"),
				new DiscardHandler(".+\\@victoriassecret.com.*"),	// email
				new DiscardHandler(".+_OF_[BF].jpg"),	// product-only back/front image
				new DiscardHandler(Regexes.GIF, true),
				new DiscardHandler(Regexes.PNG, true),
				
				new StringFunctionHandler(
					new SearchReplaceStringFunction("/product/45x60/", "/product/760x1013/"),
					new SearchReplaceStringFunction("/product/63x84/", "/product/760x1013/"),
					new SearchReplaceStringFunction("/product/224x299/", "/product/760x1013/"),
					new SearchReplaceStringFunction("/product/240x320/", "/product/760x1013/"),
					new SearchReplaceStringFunction("/product/250x333/", "/product/760x1013/"),
					new SearchReplaceStringFunction("/product/280x373/", "/product/760x1013/"),
					new SearchReplaceStringFunction("/product/404x539/", "/product/760x1013/"),
					new SearchReplaceStringFunction("/product/428x571/", "/product/760x1013/"),
					new SearchReplaceStringFunction("/product/504x672/", "/product/760x1013/")),
				
				new DiscardDuplicatesHandler(new ExtractStringFunction(".+?victoriassecret.com/.+?(ProductID=\\d+).*")),
				
				new PrintHandler(".+?victoriassecret.com/.+/\\d+x\\d+/.+jpg"),
				
				new HtmlHandler(".+?victoriassecret.com/.+ProductID.+",
					new Select("a", "href"),
					new Select("img", "src"),
					new Select("input", "data-alt-images", (element) -> {
						final List<String> images = new ArrayList<>();
						for (final String image : element.attr("data-alt-images").split(",")) {
							if (image.isEmpty() == false) {
								images.add("https://dm.victoriassecret.com/product/760x1013/" + image + ".jpg");
							}
						}
						return images;
					})),
				new HtmlHandler(".+?victoriassecret.com.*", new Select("a", "href")),
		};
		
		final Queue queue = new PriorityQueue(new ChainedComparator<>(new RegexComparator(Regexes.JPG)));
		queue.add(Arrays.asList("https://www.victoriassecret.com/"));
		
		final ChainOfResponsibility chain = new ChainOfResponsibility(handlers, queue);
		chain.start();
	}
	
}
