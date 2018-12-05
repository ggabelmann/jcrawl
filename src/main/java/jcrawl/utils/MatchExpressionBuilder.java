package jcrawl.utils;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Function;

/**
 * Temporary doc: this is also like a chain of responsibility.
 * @param <T> The input.
 * @param <R> The output.
 */
public class MatchExpressionBuilder<T, R> {

    private final List<Function<T, Optional<R>>> expressions;

    public static void main(final String[] args) {
        final MatchExpressionBuilder<String, Integer> builder = new MatchExpressionBuilder<>();
        final Function<String, Integer> function = builder
                .withCase((a) -> Optional.empty())
                .withGuard((a) -> a.length());

        System.out.println(function.apply("test"));
    }

    public MatchExpressionBuilder() {
        this.expressions = new ArrayList<>();
    }

    public MatchExpressionBuilder<T, R> withCase(final Function<T, Optional<R>> input) {
        expressions.add(input);
        return this;
    }

    public Function<T, R> withGuard(final Function<T, R> input) {
        return new MatchExpression(new ImmutableList.Builder().addAll(expressions).build(), input);
    }

    private class MatchExpression implements Function<T, R> {

        private final ImmutableList<Function<T, Optional<R>>> expressions;
        private final Function<T, R> guard;

        private MatchExpression(final ImmutableList<Function<T, Optional<R>>> expressions, final Function<T, R> guard) {
            this.expressions = expressions;
            this.guard = guard;
        }

        @Override
        public R apply(final T input) {
            for (final ListIterator<Function<T, Optional<R>>> li = expressions.listIterator(); li.hasNext(); ) {
                final Optional<R> result = li.next().apply(input);
                if (result.isPresent()) {
                    return result.get();
                }
            }

            return guard.apply(input);
        }
    }

}
