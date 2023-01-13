# Parallel Web Crawler

I was given a Java implementation of a single-threaded web crawler
and unit tests for both a single-threaded and multi-threaded implementation
of the crawler. I used Java concurrency library, third-party packages, and
creational design patterns to implement a web crawler that takes 
advantage of multiple cores to parallelize its workload.

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
### How to Run


### Open-Source Third Party Java Libraries

- jsoup
- Jackson Project
- Guice
- Maven
- JUnit 5
- Truth

### Running Tests

Run:

```
mvn test
```
