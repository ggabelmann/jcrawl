package jcrawl.handler;

import jcrawl.AbstractRegex;
import java.util.Collections;
import java.util.Optional;

/**
 * This Class discards urls which match a regex.
 */
public class DiscardHandler extends AbstractRegex implements Handler {
	
	private final boolean print;
	
	public DiscardHandler(final String regex) {
		this(regex, false);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param regex Cannot be null.
	 * @param print If true then when an url is discarded it will be printed with a preceding #.
	 */
	public DiscardHandler(final String regex, final boolean print) {
		super(regex);
		this.print = print;
	}
	
	private boolean isPrint() {
		return print;
	}

	@Override
	public Optional<Iterable<String>> handle(final String url) {
		if (getMatcher(url).matches()) {
			if (isPrint()) {
				System.out.println("# DiscardHandler: " + url);
			}
			return Optional.of(Collections.emptyList());
		}
		else {
			return Optional.empty();
		}
	}
}
