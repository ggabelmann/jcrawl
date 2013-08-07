jcrawl
======

At a high level jcrawl is a web crawler, written in java, which uses the Chain of Responsibility design pattern to control which URLs it visits and prints out. The chain is composed of Handlers which decide one-at-a-time whether or not they will handle the URL. If yes, then the handler can take some action, return a new list of URLs, or both. If no, then the next Handler has a chance to handle the URL.

Usage
=====

Currently, there is only one crawler: CrawlBootieMashup.java. It can be run directly from the commandline. It does not download anything; it only prints out URLs which are MP3s, PDFs, or ZIPs.

HTTP requests are throttled by sleeping one second before sending the request.

Future
======

Add more crawlers.

Add tests.