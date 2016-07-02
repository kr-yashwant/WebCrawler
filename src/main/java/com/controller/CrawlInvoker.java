package com.controller;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.util.CrawlParameters;

public class CrawlInvoker {
	static Logger LOGGER = Logger.getLogger(CrawlInvoker.class);
	public static void main(String[] args) {
		BasicConfigurator.configure();
		System.out.println("");
		System.out.println("");
		System.out.println("-----------------------------------------");
		System.out.println("");
		System.out.println("");
		String url = "https://github.com/kr-yashwant/";
		System.out.println("Please enter a url to start crawling with:");
		Scanner scanner = new Scanner(System.in);
		try {
			String urlInput = scanner.nextLine();
			Jsoup.connect(urlInput).get();
			url = urlInput;
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid URL detected! Using default URL: https://github.com/kr-yashwant/");
		} catch (IOException e) {
			System.out.println("Invalid URL detected! Using default URL: https://github.com/kr-yashwant/");
		}
		System.out.println("Please enter maximum number of URLs to be parsed:");
		int limit = 1000;
		try {
			limit = Integer.parseInt(scanner.nextLine());
		} catch(NumberFormatException e) {
			System.out.println("Invalid limit entered! Default limit is 1000");
		}
		System.out.println("Please enter a file name to be used a repository of URLs:");
		String fileName = scanner.nextLine()+".log";
		System.out.println("Crawling started");
		
		CrawlParameters.MAX_RECORD_OUTPUT = limit;
		CrawlParameters.MAX_ITERATION_LIMIT = limit;
		CrawlParameters.FILE_NAME= fileName;
		
		//Create the CrawlController instance 
		final CrawlController crawlController = new CrawlController();
		LOGGER.debug("CrawlController instance created");
		//Add a shutdown hook to ensure proper closure of Crawlers in case of
		//user initiated abrupt closure of the program
		//Pass the URL to the CrawlController and address it to start crawlers for it
		crawlController.addUrlToParse(url);
		crawlController.startCrawlers();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				//guide the CrawlController instance to stop all children crawlers
				//and close all resources being used by the program
				if(crawlController.isUserInterrupted()) {
					crawlController.stopCrawlers();
				}
			}
		});
	}

}
