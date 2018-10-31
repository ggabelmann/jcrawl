package jcrawl.crawl;

import com.google.common.collect.ImmutableList;
import jcrawl.core.Crawler;
import jcrawl.core.Link;
import jcrawl.core.PriorityTransformingQueue;
import jcrawl.core.Queue;
import jcrawl.fetch.*;
import jcrawl.transform.*;
import jcrawl.utils.LinkMatchers;
import jcrawl.utils.Regexes;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Crawl the music site bootiemashup.com and print out links to music files.
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

        // The queue is the heart of the crawler.
        final Queue queue = new PriorityTransformingQueue(
                // Comparator.
                (s1, s2) -> {
                    if (IS_MUSIC.test(s1) && !IS_MUSIC.test(s2)) return -1;
                    else if (!IS_MUSIC.test(s1) && IS_MUSIC.test(s2)) return 1;
                    else return 0;
                },
                // Function to transform links.
                new Trim().andThen(
                new Transform("([Hh][Tt][Tt][Pp][Ss]?://)(.*)", Optional.of(ImmutableList.of(s -> "http://", s -> s)))).andThen(
                new Substring("#")).andThen(
                new Substring("?")).andThen(
                new Transform("(.+)/", Optional.empty())).andThen(
                new Transform("(.+)/feed.*", Optional.empty())).andThen(
                new Discard(DISCARD)).andThen(
                new EncodeSpaces()));

        // The Consumer just prints out music links.
        final Consumer<Link> consumer = (s) -> {
            if (IS_MUSIC.test(s)) {
                System.out.println(s);
            }
        };

        // The Function just fetches html documents and finds links inside pages.
        final Function<Link, Set<Link>> fetchDocument = new FetchDocument(new EventsWindow(30, 60000)).andThen(new Select("a", "href"));
        final Function<Link, Set<Link>> fetcher = (link) -> {
            if (IS_FETCHABLE.test(link)) {
                return fetchDocument.apply(link);
            }
            else {
                return Collections.emptySet();
            }
        };

        // Time to start teh crawling.

        final Crawler crawler = new Crawler(
                queue.add(Collections.singleton(new Link("http://bootiemashup.com/bestof"))),
                consumer,
                fetcher);

        while (crawler.getQueue().peekHead().isPresent()) {
            final Optional<Crawler.ThrowableDetails> result = crawler.step();
            if (result.isPresent()) {
                System.out.println(String.format("# exception occurred for: %s", result.get().getLink()));
            }
        }
    }

}
