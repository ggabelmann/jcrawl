package jcrawl.fetch;

import jcrawl.core.Link;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Function;

/**
 * This class fetches a Jsoup Document.
 */
public class FetchDocument implements Function<Link, Document> {

    private final Runnable delay;

    public FetchDocument() {
        this(() -> {});
    }

    public FetchDocument(final Runnable delay) {
        this.delay = delay;
    }

    /**
     * First executes an optional delay before fetching the given link.
     */
    public Document apply(final Link link) {
        delay.run();
        try {
            final long start = System.currentTimeMillis();
            System.out.println(String.format("# Fetching %s", link));
            final Document result = Jsoup.connect(link.getValue()).timeout(1000 * 30).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20100101 Firefox/15.0.1").get();
            System.out.println(String.format("# Fetch took %d ms ", (System.currentTimeMillis() - start)));
            return result;
        }
        catch (final IOException ex1) {
            throw new UncheckedIOException(ex1);
        }
    }

}
