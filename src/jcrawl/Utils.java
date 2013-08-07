package jcrawl;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * This Class has utility methods.
 */
public class Utils {
	
	public static String orRegexes(final String... regexes) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < regexes.length - 1; i++) {
			sb.append(regexes[i]);
			sb.append("|");
		}
		sb.append(regexes[regexes.length - 1]);
		return sb.toString();
	}
	
	public static String pad(final int number, final int length) {
		return pad(Integer.toString(number), length);
	}
	
	public static String pad(final String number, final int length) {
		String working = number;
		while (working.length() < length) working = "0" + working;
		return working;
	}
	
	public static URL createUrl(final String url) {
		try {
			return new URL(url);
		}
		catch (final MalformedURLException ex1) {
			throw new RuntimeException(ex1);
		}
	}
	
	/**
	 * Waits 1 second before attempting to fetch the url.
	 * Timeout is 30 seconds.
	 * 
	 * @param url Cannot be null.
	 * @return The Document.
	 */
	public static Document fetchAsDocument(final String url) {
		try {
			Thread.sleep(1000);
			System.out.println("# Fetching " + url);
			return Jsoup.connect(url).timeout(1000 * 30).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20100101 Firefox/15.0.1").get();
		}
		catch (final Exception ex1) {
			throw new RuntimeException(ex1);
		}
	}
	
}
