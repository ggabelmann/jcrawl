jcrawl
======

At a high level jcrawl is a web crawler, written in java, that tries to follow the principles of "functional core, imperative shell".
(See https://www.destroyallsoftware.com/talks/boundaries for a great video about it.)
That is, most objects are functional in nature (immutable and without side-effects) and I/O is kept in an imperative shell.

The codebase is currently not threadsafe mostly because I don't need it to be threadsafe.
Making it threadsafe will require some work in the imperative shell.

It has a pluggable scheduler that can, for example, maintain a sliding window over events so that the crawler doesn't overwhelm the target server.

Usage
=====

Currently, there is only one crawler: CrawlBootieMashup.java.
It can be run directly from the commandline.
It does not download anything; it only prints out URLs which are likely to be music.

Future
======

Make parts of it threadsafe.

Add more crawlers.

Add tests.