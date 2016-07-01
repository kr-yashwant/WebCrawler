WebCrawler is a MultiThreaded URL crawling utility that parses a given URL for the document and scans the document for 
any URLs it finds in the page. It saves the unique URLs hence found in a file.

The project uses CrawlInvoker to invoke the CrawlController that controls multiple threads by implementing Crawlers. These 
crawlers parse a specific URL each and then save the unique URLs to a file which has to be declared in Constants.java.

The crawling stops in two conditions- either on reaching the maximum number of unique URLs to be parsed or upon forced stop
by user.

The file used to flush the data here is crawlLog.dat

All events related to the Project are being logged in WebCrawler.log

Currently the project has to be built using Eclipse. All related dependencies are mentioned in .classpath file and are in 
lib folder of the project.

Maven dependencies are yet to be declared. So, invoking mvn install may cause errors in build.
