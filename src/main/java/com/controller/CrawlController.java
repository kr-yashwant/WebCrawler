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

import com.crawler.Crawler;
import com.util.Constants;
import com.util.Printer;

public class CrawlController {

    private BlockingQueue<String> queueOfUrls = new ArrayBlockingQueue<String>(Constants.MAX_QUEUE_SIZE);
    private ExecutorService executorService = new ThreadPoolExecutor(0, 10, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
    private Printer printer =  new Printer(Constants.FILE_NAME);
    private Set<String> parsedUrls = new HashSet<String>();
	private AtomicInteger iterationCount = new AtomicInteger(0);

    public void startCrawler() {
		try {
	    	while(!queueOfUrls.isEmpty() || !(iterationCount.get() < Constants.MAX_ITERATION_LIMIT)) {
				Crawler crawler = new Crawler(this, queueOfUrls.take());
				executorService.submit(crawler);
				
				if(queueOfUrls.isEmpty()) {
					cyclicBarrier.await();
				}
	    	}
		} catch (InterruptedException e) {
			System.err.println("Exception"+e.getClass()+" occurred while trying to fetch from Queue of URLs");
		} catch (BrokenBarrierException e) {
			System.err.println("Exception"+e.getClass()+" occurred while trying wait for entries in Queue of URLs");
		} finally {
			shutDownExecutorService();
		}
    }
    
    public void shutDownExecutorService() {
    	try {
			this.executorService.shutdown();
			this.executorService.awaitTermination(5000, TimeUnit.NANOSECONDS);
			this.printer.close();
			System.exit(1);
		} catch (InterruptedException e) {
			System.err.println("Exception"+e.getClass()+" occurred while waiting for crawlers to stop");
		}
    }
    
    public void addToQueueOfUrls(String url) {
    	this.queueOfUrls.add(url);
    }
    
    public CyclicBarrier getCyclicBarrier() {
    	return this.cyclicBarrier;
    }
    
    public Printer getPrinter() {
    	return this.printer;
    }

	public Set<String> getParsedUrls() {
		return parsedUrls;
	}

	public int getIterationCount() {
		return iterationCount.get();
	}

	public void setIterationCount(int iterationCount) {
		this.iterationCount.set(iterationCount);
	}
	
	public synchronized void incrementIterationCount() {
		this.iterationCount.getAndIncrement();
	}
	
	
}
