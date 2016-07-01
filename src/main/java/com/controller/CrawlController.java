package com.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.crawler.Crawler;
import com.util.Constants;
import com.util.Printer;
import com.util.UrlUtil;

/**
 * @author kryas
 *
 */
public class CrawlController {
	
	private Logger LOGGER = Logger.getLogger(CrawlController.class);

    private BlockingQueue<String> queueOfUrls = new ArrayBlockingQueue<String>(Constants.MAX_QUEUE_SIZE);
    private ExecutorService executorService = new ThreadPoolExecutor(0, 10, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
    private Printer printer =  new Printer(Constants.FILE_NAME);
    private Set<String> parsedUrls = new HashSet<String>();
	private AtomicInteger iterationCount = new AtomicInteger(0);

    /**
     * Declares crawlers for the contents of the queue adds them to the executor service 
     * and initiates their crawling
     * Keeps adding new Crawlers to the executorService until either the queue becomes 
     * empty or the Maximum Iteration Limit is achieved
     * If the queue of URLs becomes empty, it waits for the child Crawlers to add URLs to
     * it at the barrier.
     */
    public void startCrawlers() {
		try {
			/* Check if the FIFO has any URLs left to parse and parse it upto allowed iteration limit */
	    	while(!queueOfUrls.isEmpty() || !(iterationCount.get() < Constants.MAX_ITERATION_LIMIT)) {
				Crawler crawler = new Crawler(this, queueOfUrls.take());
				executorService.submit(crawler);
				LOGGER.debug("New Crawler submitted");
				
				if(queueOfUrls.isEmpty()) {
					cyclicBarrier.await();
				}
	    	}
		} catch (InterruptedException e) {
			LOGGER.error("Exception"+e.getClass()+" occurred while trying to fetch from Queue of URLs");
		} catch (BrokenBarrierException e) {
			LOGGER.error("Exception"+e.getClass()+" occurred while trying wait for entries in Queue of URLs");
		} finally {
			stopCrawlers();
		}
    }
    
    /**
     * shuts down all the Crawlers attached to the CrawlController instance
     * and exits the program
     */
    public void stopCrawlers() {
    	try {
			this.executorService.shutdown();
			if (this.executorService.awaitTermination(5000, TimeUnit.NANOSECONDS)) {
			  LOGGER.debug("Crawlers closed within 5 secs");
			} else {
			  LOGGER.debug("Shutting down unclosed Crawlers forcefully");
			  this.executorService.shutdownNow();
			}
			LOGGER.debug("Parsed URLs");
			for(String url : this.getParsedUrls()) {
				LOGGER.debug("url -> "+url);
			}
			this.printer.close();
			LOGGER.debug("Program ended safely");
			System.exit(1);
			Logger.shutdown();
		} catch (InterruptedException e) {
			LOGGER.error("Exception"+e.getClass()+" occurred while waiting for crawlers to stop");
		}
    }
    
    /**
     * adds purpose the CrawlController instance by giving it
     * a URL to parse. The content will be added to the queue 
     * of tasks and passed to a Crawler to crawl upon
     */
    public void addUrlToParse(String url) {
    	this.queueOfUrls.add(UrlUtil.sanitiseUrl(url));
    }
    
    public CyclicBarrier getCyclicBarrier() {
    	return this.cyclicBarrier;
    }
    
    /**
     * for the children Crawlers to flush the names of the parsed
     * URLs to a file
     */
    public Printer getPrinter() {
    	return this.printer;
    }

	/**
	 * returns the collection of URLs which have been parsed
	 * by the Crawlers controller by CrawlController instance
	 */
	public synchronized Set<String> getParsedUrls() {
		return parsedUrls;
	}

	/**
	 * returns the iterator status at the time of 
	 * method call
	 */
	public int getIterationCount() {
		return iterationCount.get();
	}

	/**
	 * can be used to modify the iterator contents if needed
	 */
	public void setIterationCount(int iterationCount) {
		this.iterationCount.set(iterationCount);
	}
	
	/**
	 * increments the iterator to comply with the maximum 
	 * parsing condition
	 */
	public synchronized void incrementIterationCount() {
		this.iterationCount.getAndIncrement();
	}
	
	/**
	 * adds url string to the collection of parsed urls to 
	 * which indicates that the URL is already parsed
	 */
	public synchronized void addParsedUrl(String url) {
		this.parsedUrls.add(url);
	}
	
	
	/**
	 * returns true if a the url or a sanitized form of it is already
	 * a member of the parsed URL collection
	 */
	public boolean contains(String url) {
		return this.parsedUrls.contains(UrlUtil.sanitiseUrl(url));
	}

	public void reportParsing(String obtainedUrl) {
		this.getPrinter().write(obtainedUrl);
		this.getPrinter().flush();
	}
	
	
}
