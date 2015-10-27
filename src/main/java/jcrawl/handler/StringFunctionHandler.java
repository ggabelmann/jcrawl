package jcrawl.handler;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

/**
 * This Class wraps a StringFunction so that it can be used in a ChainOfResponsibility.
 */
public class StringFunctionHandler implements Handler {
	
	private final Function<String, String> function;
	
	public StringFunctionHandler(final Function<String, String>... functions) {
		// Must be Function<String, String> because it can't be UnaryOperator<String> because 
		// andThen() returns a Function<>, not a UnaryOperator.
		Function<String, String> andThen = null;
		for (Function<String, String> f : functions) {
			if (andThen == null) {
				andThen = f;
			}
			else {
				andThen = andThen.andThen(f);
			}
		}
		this.function = andThen;
	}
	
	/**
	 * If the transformed url is not changed then null will be returned (that is, the url was not handled).
	 * Otherwise an Iterator is returned with the transformed url (so, it was handled).
	 */
	@Override
	public Optional<Iterable<String>> handle(final String url) {
		final String transformed = function.apply(url);
		
		if (transformed.equals(url)) {
			return Optional.empty();
		}
		else {
			return Optional.of(Collections.singletonList(transformed));
		}
	}

}
