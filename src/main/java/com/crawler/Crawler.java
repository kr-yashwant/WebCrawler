package com.crawler;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;

import javax.net.ssl.SSLHandshakeException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.controller.CrawlController;
import com.util.Constants;

/**
 * @author kryas
 * declares a single Crawler Thread that will parse the urlPassed to it while 
 * instantiation and will report the URLs passed to the Controller that it 
 * answers to
 *
 */
public class Crawler implements Runnable{
	Logger LOGGER = Logger.getLogger(Crawler.class);

	private String urlPassed;	private CrawlController controller;
	
	public Crawler(CrawlController controller, String urlPassed) {
		super();
		this.urlPassed = urlPassed;
		this.controller = controller;
	}
	
	/**
	 * crawls
	 * i.e. visits the urlPassed and fetches the document,
	 * then fetches all the URLs from it and adds unique ones
	 * to its parent controller in case they haven't already been
	 * parsed.
	 * 
	 * may stop execution in case of an InterruptException or 
	 * BrokenBarrierException while waiting at the barrier
	 * 
	 * may stop execution in case of an SSLHandshakeException
	 * occurrence while trying to connect to a URL
	 */
	public void crawl() throws IOException {
		try {
			//Parsing the passed URL into Document instance
			Document document = Jsoup.connect(this.urlPassed).get();
			//Searching for a:href in the Document instance
			Elements links = document.select("a[href]");
			parseLoop: for(Element link: links) {
				//Printing the links to the 
				String obtainedUrl = link.attr("abs:href");
				if(!this.controller.contains(obtainedUrl)) {
					LOGGER.debug(obtainedUrl);
					this.controller.addParsedUrl(obtainedUrl);
					this.controller.addUrlToParse(obtainedUrl);
					reportParsing(obtainedUrl);
					this.controller.incrementIterationCount();
				}
				if(!(this.controller.getIterationCount() < Constants.MAX_ITERATION_LIMIT)) {
					this.controller.stopCrawlers();
					break parseLoop;
				}
			}
			if(this.controller.getCyclicBarrier().getNumberWaiting()==1){
		        this.controller.getCyclicBarrier().await();
		        
		    }
		} catch (InterruptedException e) {
			LOGGER.error("Exception"+e.getClass()+" occurred while waiting at barrier");
		} catch (BrokenBarrierException e) {
			LOGGER.error("Exception"+e.getClass()+" occurred while waiting at barrier");
		} catch(SSLHandshakeException e) {
			LOGGER.error("Could not connect to " +this.urlPassed+ " due to SSLHandshakeException ");
		}
	}
	
	/**
	 * reports the URL obtained while parsing to the controller 
	 * being answered
	 */
	public void reportParsing(String obtainedUrl) {
		this.controller.reportParsing(obtainedUrl);
	}

	@Override
	public void run() {
		try {
			crawl();
		} catch (IOException e) {
			LOGGER.error("An error occurred while trying to parse " +this.urlPassed);
			e.printStackTrace();
		}
	}
}
