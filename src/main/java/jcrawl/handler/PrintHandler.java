package jcrawl.handler;

import jcrawl.AbstractRegex;
import jcrawl.Utils;
import java.util.Collections;
import java.util.Optional;

/**
 * This Class prints urls which match a regex.
 */
public class PrintHandler extends AbstractRegex implements Handler {
	
	public PrintHandler(final String... regexes) {
		super(Utils.orRegexes(regexes));
	}
	
	@Override
	public Optional<Iterable<String>> handle(final String url) {
		if (getMatcher(url).matches()) {
			System.out.println(url);
			return Optional.of(Collections.emptyList());
		}
		else {
			return Optional.empty();
		}
	}

}

