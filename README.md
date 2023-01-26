# Parallel Web Crawler

I was given a Java implementation of a single-threaded web crawler
and unit tests for both a single-threaded and multi-threaded implementation
of the crawler. I used Java concurrency library, third-party packages, and
creational design patterns to implement a web crawler that takes 
advantage of multiple cores to parallelize its workload.

### Creational Patterns and Libraries Used, Bugs Squashed
- Used Jackson to serialize and deserialize data from an Object to a JSON in `java/com/udacity/webcrawler/json/*.java`
- Used Guice to reuse objects using dependency injection in `java/com/udacity/webcrawler/ParallelWebCrawler.java`
- Used Builder pattern in `java/com/udacity/webcrawler/CrawlAction.java`
- Used ForkJoinPool and recursive actions in `java/com/udacity/webcrawler/CrawlAction.java,ParallelWebCrawler.java`
- Used Factory pattern in `java/com/udacity/webcrawler/CrawlActionFactory.java,CrawlActionFactoryImpl.java`
- Used synchronized collections in `java/com/udacity/webcrawler/ParallelWebCrawler.java`
- Used lock on set in `java/com/udacity/webcrawler/CrawlAction.java`
- Used Streams API and lambda function to eliminate verbose for loop to find the top `k` words with the highest count across all pages crawled in
`java/com/udacity/webcrawler/WordCounts.java`
- Squashed a bug in original `src` code where `isAfter` method was not working for nanoseconds after. Switched to
`compareTo` method for judging if `Instant` is after `Instant deadline`.

### How to Run

Clone and run the following to run:

```
mvn package
java -classpath target/udacity-webcrawler-1.0.jar com.udacity.webcrawler.main.WebCrawlerMain src/main/config/sample_config.json
```

### Configuration File

This is a sample configuration JSON given to the web crawler.
```
{
  "startPages": ["http://example.com", "http://example.com/foo"],
  "ignoredUrls": ["http://example\\.com/.*"], 
  "ignoredWords": ["^.{1,3}$"], 
  "parallelism": 4, 
  "implementationOverride": "com.udacity.webcrawler.SequentialWebCrawler", 
  "maxDepth": 10, 
  "timeoutSeconds": 7, 
  "popularWordCount": 3, 
  "profileOutputPath": "profileData.txt" 
  "resultPath": "crawlResults.json" 
}


/**
 * Notes:
 * ignoredUrls and ignoredWords use regex, which in Java is an instance of the Pattern class.
 * parallelism is the number of desired threads, and is either that or defaults to number of available CPU cores.
 * implementation override overrides parallelism (which invokes parallel web crawler if > 1). It can be 
 * either SequentialWebCrawler or ParallelWebCrawler.
 * maxDepth is the hardcoded depth of the search trie, the program terminates at a further depth.
 * The two paths are where to write performance data and the results. If unset, these are printed to standard output.
 */
```


### Open-Source Third Party Java Libraries

- jsoup
- Jackson Project
- Guice
- Maven
- JUnit 5
- Truth

### Takeaway

Overall, this was a fun use case for practicing more 
complex Java patterns and doing some difficult debugging.
