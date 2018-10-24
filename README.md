jcrawl
======

At a high level jcrawl is a web crawler, written in java, that tries to follow the principles of "functional core, imperative shell".
(See https://www.destroyallsoftware.com/talks/boundaries for a great video about it.)
That is, most objects are functional in nature (immutable and without side-effects) and I/O is kept in an imperative shell.

Usage
=====

Currently, there is only one crawler: CrawlBootieMashup.java.
It can be run directly from the commandline.
It does not download anything; it only prints out URLs which are likely to be music.
HTTP requests are throttled by sleeping one second before sending the request.

Future
======

Add more crawlers.

Add tests.