package jcrawl.handler.document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This Class extracts urls from Documents..
 */
public class Select implements DocumentSelectStrategy {
	
   final String attribute;
   final String tag;
   
   /**
    * Constructor.
    * 
    * @param tag The html tag. Cannot be null.
    * @param attribute The attribute of the tag. Cannot be null.
    */
   public Select(final String tag, final String attribute) {
      this.tag = tag;
      this.attribute = attribute;
   }
   
	@Override
	public Iterator<String> getUrls(final Document document) {
		final List<String> urls = new ArrayList<String>();
		final Elements hrefs = document.select(tag + "[" + attribute + "]");

		for (final Element element : hrefs) {
			urls.add(element.attr("abs:" + attribute));
		}
		return urls.iterator();
	}
	
}
