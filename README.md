At a high level jcrawl is a web crawler, written in java, that tries to follow the principles of "functional core, imperative shell".
(See https://www.destroyallsoftware.com/talks/boundaries for a great video about it.)
That is, most objects are functional in nature (immutable and without side-effects) and I/O is kept in an imperative shell.

Multi-threaded crawls are supported by using a parallel Stream. The Crawler.java class implements the original design which is not threadsafe, but could be made so in the future. Things are still evolving.

It has a pluggable scheduler that can, for example, maintain a sliding window over events so that the crawler doesn't overwhelm the target server.

## Usage

Currently, there is only one crawler: CrawlBootieMashup.java. (http://bootiemashup.com is a music site.)
It can be run directly from the commandline.
It does not download anything; it only prints out URLs which are likely to be music.

## Future

* Add more crawlers.
* Add unit tests for the functional core.

## Public Service Announcement

I'm looking for full-time and/or contract work! My [Github Profile](https://ggabelmann.github.com) has more details on my skills, how I like to work, and my resume. Drop me a line and we can chat about working together.
