package com.test;

import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;

import com.controller.CrawlInvoker;

public class CrawlInvocationTester {
	public static void main(String[] args) {
		/*BasicConfigurator.configure();
		CrawlInvoker crawlInvoker = new CrawlInvoker();
		System.out.println("");
		System.out.println("");
		System.out.println("-----------------------------------------");
		System.out.println("");
		System.out.println("");
		System.out.println("Please enter a url to start crawling with:");
		Scanner scanner = new Scanner(System.in);
		String url = scanner.nextLine();
		System.out.println("Please enter maximum number of URLs to be parsed:");
		int limit = 1000;
		try {
			limit = Integer.parseInt(scanner.nextLine());
		} catch(NumberFormatException e) {
			System.out.println("Invalid limit entered! Default limit is 1000");
		}
		System.out.println("Please enter a file name to be used a repository of URLs:");
		String fileName = scanner.nextLine()+".dat";
		System.out.println("Crawling started");
		crawlInvoker.runCrawlController(url, limit, fileName);*/
	}

}
