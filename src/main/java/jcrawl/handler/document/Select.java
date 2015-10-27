package jcrawl.handler.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This Class extracts urls from Documents..
 */
public class Select implements DocumentSelectStrategy {
	
   final String attribute;
   final String tag;
   final Function<Element, Iterable<String>> f;
	
   /**
    * Constructor.
    * 
    * @param tag The html tag. Cannot be null.
    * @param attribute The attribute of the tag. Cannot be null.
    */
   public Select(final String tag, final String attribute) {
		this(tag, attribute, (element) -> Collections.singletonList(element.attr("abs:" + attribute)));
   }
   
   public Select(final String tag, final String attribute, final Function<Element, Iterable<String>> f) {
      this.tag = tag;
      this.attribute = attribute;
		this.f = f;
   }
	
	@Override
	public Iterable<String> getUrls(final Document document) {
		final List<String> urls = new ArrayList<>();
		final Elements hrefs = document.select(tag + "[" + attribute + "]");

		for (final Element element : hrefs) {
			for (final String url : f.apply(element)) {
				urls.add(url);
			}
		}
		return urls;
	}
	
}
