package com.controller;

public class CrawlInvoker {
	public static void main(String[] args) {

		//Pass the URL to be parsed in the value of the url string
		String url = "http://stackoverflow.com/questions/2975248/java-how-to-handle-a-sigterm";
		//Create the CrawlController instance 
		CrawlController crawlController = new CrawlController();
		//Add a shutdown hook to ensure proper closure of Crawlers in case of
		//user initiated abrupt closure of the program
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				//guide the CrawlController instance to stop all children crawlers
				//and close all resources being used by the program
				crawlController.stopCrawlers();
			}
		});
		//Pass the URL to the CrawlController and address it to start crawlers for it
		crawlController.addUrlToParse(url);
		crawlController.startCrawlers();
	}

}
