package com.udacity.webcrawler;

import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.parser.PageParserFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.udacity.webcrawler.parser.PageParser;

/**
 * A concrete implementation of {@link WebCrawler} that runs multiple threads on a
 * {@link ForkJoinPool} to fetch and process multiple web pages in parallel.
 */
final class ParallelWebCrawler implements WebCrawler {
  private final Clock clock;
  private final Duration timeout;
  private final int popularWordCount;
  private final ForkJoinPool pool;

  private final int maxDepth;

  private final List<Pattern> ignoredUrls;


  @Inject
  ParallelWebCrawler(
      Clock clock,
      @Timeout Duration timeout,
      @PopularWordCount int popularWordCount,
      @TargetParallelism int threadCount,
      @MaxDepth int maxDepth,
      @IgnoredUrls List<Pattern> ignoredUrls) {
    this.clock = clock;
    this.timeout = timeout;
    this.popularWordCount = popularWordCount;
    this.pool = new ForkJoinPool(Math.min(threadCount, getMaxParallelism()));
    this.maxDepth = maxDepth;
    this.ignoredUrls = ignoredUrls;
  }

  @Inject
  private PageParserFactory parserFactory;

  private CrawlActionFactoryImpl crawlFactory;

  static Map<String, Integer> counts = Collections.synchronizedMap(new HashMap<>());
  static Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>());

  @Override
  public CrawlResult crawl(List<String> startingUrls) {
    Instant deadline = clock.instant().plus(timeout);

    System.out.println("refr to original visitedurls" + visitedUrls);

    //fix crawl factory instantiation to hide Impl? dependency injection?
    crawlFactory = new CrawlActionFactoryImpl(deadline, clock, maxDepth, ignoredUrls);
    //System.out.println(crawlFactory.get(startingUrls.get(0)).toString());

    for (String url : startingUrls) {
      pool.execute(crawlFactory.get(url));
    }

    System.out.println("Visited urls" +  visitedUrls.toString());

    if (counts.isEmpty()) {
      return new CrawlResult.Builder()
              .setWordCounts(counts)
              .setUrlsVisited(visitedUrls.size())
              .build();
    }

    return new CrawlResult.Builder()
            .setWordCounts(WordCounts.sort(counts, popularWordCount))
            .setUrlsVisited(visitedUrls.size())
            .build();
  }


  @Override
  public int getMaxParallelism() {
    return Runtime.getRuntime().availableProcessors();
  }


}
