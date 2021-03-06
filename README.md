# WebCrawler

Purpose:
--------
WebCrawler is a MultiThreaded URL crawling utility that parses a given URL for the document and scans the document for 
any URLs it finds in the page. It saves the unique URLs hence found in a file.

Input Specifics:
---------------
The project uses CrawlInvoker to invoke the CrawlController that controls multiple threads by implementing Crawlers. These 
crawlers parse a specific URL each and then save the unique URLs to a file which has to be determined from user input.


Execution Specifics:
-------------------
The crawling stops in two conditions- either on reaching the maximum number of unique URLs to be parsed or upon forced stop
by user.


Output Specifics:
----------------
The file used to flush the data here is taken from user input

All events related to the Project are being logged in WebCrawler.log

How to run:
-----------------------
Download WebCrawler-1.0-jar-with-dependencies.jar and invoke java -jar WebCrawler-1.0-jar-with-dependencies.jar using
a command line. (e.g.- cmd, bash etc.)

Alternatively, runCrawler.bat or runCrawler.sh can be invoked depending upon the desired platforms.


Give the URL, maximum number of URLs to be written and a file name to which the obtained URLs are to be logged
The output will be saved in the file with a .log extension.
