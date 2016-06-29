package com.controller;

public class CrawlControllerTest {
	public static void main(String[] args) {
		String url = "http://python.org";
		/*String url = "http://wiki.python.org/moin/Languages";*/
        CrawlController crawler  = new CrawlController();
        crawler.addToQueueOfUrls(url);
        crawler.startCrawler();
	}
}
