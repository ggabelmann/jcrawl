package jcrawl.handler;

import jcrawl.stringfunction.ExtractStringFunction;
import java.util.Collections;
import java.util.Optional;

/**
 * The error handling of this Class is not very good.
 */
public class GalleryHandler implements Handler {
	
	private final HtmlHandler htmlHandler;
	private final ExtractStringFunction prefix;
	private final ExtractStringFunction number;
	private final ExtractStringFunction postfix;
	
	/**
	 * ?
	 * 
	 * @param aa
	 * @param prefix
	 * @param prefix
	 * @param prefix
	 */
	public GalleryHandler(final HtmlHandler aa, final ExtractStringFunction prefix, final ExtractStringFunction number, final ExtractStringFunction postfix) {
		this.htmlHandler = aa;
		this.prefix = prefix;
		this.number = number;
		this.postfix = postfix;
	}
	
	private Optional<Iterable<String>> handleHelper(final Iterable<String> urls) {
		String thePrefix = null;
		String thePostfix = null;
		int largestInt = Integer.MIN_VALUE;
		String largestString = null;
		int smallestInt = Integer.MAX_VALUE;
		String smallestString = null;

		for (final String image : urls) {
			final String imagePrefix  = this.prefix.apply(image);
			final String imageNumber  = this.number.apply(image);
			final String imagePostfix = this.postfix.apply(image);
			
			if (imagePrefix.equals(image) || imageNumber.equals(image) || imagePostfix.equals(image)) {
				continue;
			}
			
			thePrefix = imagePrefix;
			thePostfix = imagePostfix;
			
			final int imageInt = Integer.parseInt(imageNumber);

			if (imageInt > largestInt) {
				largestInt = imageInt;
				largestString = imageNumber;
			}
			if (imageInt < smallestInt) {
				smallestInt = imageInt;
				smallestString = imageNumber;
			}
		}
		
		if (largestInt >= smallestInt) {
			return Optional.of(Collections.singletonList(thePrefix + "[" + smallestString + "-" + largestString + "]" + thePostfix));
//			return Iterators.singletonIterator(thePrefix + "[" + smallestString + "-" + largestString + "]" + thePostfix);
		}
		else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Iterable<String>> handle(final String url) {
		final Optional<Iterable<String>> urls = htmlHandler.handle(url);
		if (urls.isPresent()) {
			return handleHelper(urls.get());
		}
		else {
			return urls;	// Empty.
		}
		
//		final Iterator<String> it = htmlHandler.handle(url);
//		if (it == null) {
//			return null;
//		}
//		else {
//			return handleHelper(it);
//		}
	}
}

