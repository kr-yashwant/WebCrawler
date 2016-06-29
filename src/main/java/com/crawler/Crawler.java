package com.crawler;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.controller.CrawlController;
import com.util.Constants;

public class Crawler implements Runnable{

	private String urlPassed;	private CrawlController controller;
	
	public Crawler(CrawlController controller, String urlPassed) {
		super();
		this.urlPassed = urlPassed;
		this.controller = controller;
	}
	
	/**
	 * @author kryas
	 * Implements main crawling logic using JSoup for 
	 * parsing the html from the passed URL
	 * @throws IOException 
	 */
	public void crawl() throws IOException {
		try {
			//Parsing the passed URL into Document instance
			Document document = Jsoup.connect(this.urlPassed).get();
			//Searching for a:href in the Document instance
			Elements links = document.select("a[href]");
			parseLoop: for(Element link: links) {
				//Printing the links to the standard output console
				String obtainedUrl = link.attr("abs:href");
				if(!this.controller.getParsedUrls().contains(obtainedUrl)) {
					System.out.println(obtainedUrl);
					this.controller.getParsedUrls().add(obtainedUrl);
					this.controller.addToQueueOfUrls(obtainedUrl);
					this.controller.getPrinter().write(obtainedUrl);
					this.controller.getPrinter().flush();
				}
				this.controller.incrementIterationCount();
				if(!(this.controller.getIterationCount() < Constants.MAX_ITERATION_LIMIT)) {
					this.controller.shutDownExecutorService();
					break parseLoop;
				}
			}
			if(this.controller.getCyclicBarrier().getNumberWaiting()==1){
		        this.controller.getCyclicBarrier().await();
		        
		    }
		} catch (InterruptedException e) {
			System.err.println("Exception"+e.getClass()+" occurred while waiting at barrier");
		} catch (BrokenBarrierException e) {
			System.err.println("Exception"+e.getClass()+" occurred while waiting at barrier");
		} catch(javax.net.ssl.SSLHandshakeException e) {
			System.err.println("Could not connect to " +this.urlPassed+ " due to SSLHandshakeException ");
		}
	}

	@Override
	public void run() {
		try {
			crawl();
		} catch (IOException e) {
			System.err.println("An error occurred while trying to parse " +this.urlPassed);
			e.printStackTrace();
		}
	}
}
