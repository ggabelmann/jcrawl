package jcrawl.handler;

import jcrawl.AbstractRegex;
import jcrawl.Utils;
import java.util.Collections;
import java.util.Optional;

/**
 * This Class keeps urls which match a regex.
 */
public class KeepHandler extends AbstractRegex implements Handler {
	
	public KeepHandler(final String... regexes) {
		super(Utils.orRegexes(regexes));
	}
	
	@Override
	public Optional<Iterable<String>> handle(final String url) {
		if (getMatcher(url).matches()) {
			return Optional.empty();
		}
		else {
			// System.out.println("# KeepHandler: " + url);
			return Optional.of(Collections.emptyList());
		}
	}
	
}
