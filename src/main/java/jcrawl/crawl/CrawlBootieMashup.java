package jcrawl.crawl;

import com.google.common.collect.ImmutableList;
import jcrawl.core.Link;
import jcrawl.core.PriorityTransformingQueue;
import jcrawl.core.Queue;
import jcrawl.delay.BlockingWindow;
import jcrawl.delay.CompositeWindow;
import jcrawl.delay.Window;
import jcrawl.delay.SlidingWindow;
import jcrawl.fetch.*;
import jcrawl.transform.*;
import jcrawl.utils.LinkMatchers;
import jcrawl.utils.Regexes;
import org.jsoup.nodes.Document;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Crawl the music site bootiemashup.com and print out links to music files.
 *
 * This class uses a parallel Stream instead of a single-threaded {@link jcrawl.core.Crawler} as a demonstration of what's possible.
 */
public class CrawlBootieMashup {

    private static final Predicate<Link> DISCARD = LinkMatchers.or(
            ".+BlogBacklinkURL.+",
            ".*/nggallery/.*", // just JPGs
            ".*/parties/.*", // does not seem to be any music
            ".*/wp-content/.*", // maybe some music, need to check?
            Regexes.JPG,
            Regexes.PDF,
            Regexes.PNG);
    private static final Predicate<Link> IS_MUSIC = LinkMatchers.or(Regexes.M3U, Regexes.M4A, Regexes.MP3, Regexes.ZIP);
    private static final Predicate<Link> IS_FETCHABLE = LinkMatchers.of("http://bootiemashup.com.*").and(IS_MUSIC.negate());

    public static void main(final String[] args) {
        new CrawlBootieMashup().start();
    }

    public CrawlBootieMashup() {
    }

    private void start() {
        Queue queue = getQueue().addAll(Collections.singleton(new Link("http://bootiemashup.com/bestof")));
        // Get a fetcher and keep it because it has a stateful sliding window.
        final Function<Link, Optional<Document>> fetcher = getFetcher();

        // Time to start teh crawling.
        // Note: There is some confusion online about what happens when a RuntimeException happens in a parallel stream.
        // We will be careful.

        while (queue.peekHead().isPresent()) {
            final Set<Link> newLinks = StreamSupport.stream(queue.getSpliterator(), true)
                    .peek(getPrinter())
                    .map(fetcher)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(new Select("a", "href"))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());

            queue = queue.remove((link) -> true).addAll(newLinks);
        }
    }

    private Comparator<Link> getComparator() {
        return (link1, link2) -> {
            if (IS_MUSIC.test(link1) && !IS_MUSIC.test(link2)) return -1;
            else if (!IS_MUSIC.test(link1) && IS_MUSIC.test(link2)) return 1;
            else return 0;
        };
    }

    /**
     * The queue is the heart of the crawler.
     */
    private Queue getQueue() {
        return new PriorityTransformingQueue(
                getComparator(),
                // A single Function to transform links.
                new Trim().andThen(
                new Transform("([Hh][Tt][Tt][Pp][Ss]?://)(.*)", Optional.of(ImmutableList.of(s -> "http://", s -> s)))).andThen(
                new Substring("#")).andThen(
                new Substring("?")).andThen(
                new Transform("(.+?)/+", Optional.empty())).andThen(
                new Transform("(.+)/feed.*", Optional.empty())).andThen(
                new Discard(DISCARD)).andThen(
                new EncodeSpaces()));
    }

    /**
     * The Consumer just prints out music links.
     */
    private Consumer<Link> getPrinter() {
        return (link) -> {
            if (IS_MUSIC.test(link)) {
                System.out.println(String.format("thread: %s, url: %s", Thread.currentThread().getName(), link));
            }
        };
    }

    /**
     * The Function just fetches html documents.
     */
    private Function<Link, Optional<Document>> getFetcher() {
        final Window window = new BlockingWindow(new CompositeWindow(new SlidingWindow(1, Duration.ofSeconds(1))));
        final Function<Link, Document> fetchDocument = new FetchDocument();

        return (link) -> {
            if (IS_FETCHABLE.test(link)) {
                try {
                    // In this case, don't need to reassign window because BlockingWindow returns itself.
                    window.addEvent(System.currentTimeMillis());
                    return Optional.of(fetchDocument.apply(link));
                }
                catch (final RuntimeException e) {
                    System.out.println(String.format("# exception occurred for: %s", link));
                }
            }

            return Optional.empty();
        };
    }

}
