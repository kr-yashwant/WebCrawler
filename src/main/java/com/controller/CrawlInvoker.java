package com.controller;

public class CrawlInvoker {
	public static void main(String[] args) {

		String url = "http://stackoverflow.com/questions/2975248/java-how-to-handle-a-sigterm";
		CrawlController crawlController = new CrawlController();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				crawlController.stopCrawlers();
			}
		});

		crawlController.addUrlToParse(url);
		crawlController.startCrawlers();
	}

}
