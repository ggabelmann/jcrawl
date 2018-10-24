package jcrawl.fetch;

import jcrawl.core.Link;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.function.Function;

/**
 * This Class extracts links from Documents.
 */
public class Select implements Function<Document, Set<Link>> {
	
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
   
   private Select(final String tag, final String attribute, final Function<Element, Iterable<String>> f) {
      this.tag = tag;
      this.attribute = attribute;
		this.f = f;
   }
	
	@Override
	public Set<Link> apply(final Document document) {
		final Set<Link> links = new HashSet<>();
		final Elements elements = document.select(tag + "[" + attribute + "]");

		for (final Element element : elements) {
			for (final String link : f.apply(element)) {
				links.add(new Link(link));
			}
		}
		return links;
	}
	
}
