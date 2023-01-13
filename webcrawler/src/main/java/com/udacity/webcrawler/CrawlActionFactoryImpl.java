package com.udacity.webcrawler;

import com.udacity.webcrawler.parser.PageParserFactory;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.time.Instant;
import java.util.regex.Pattern;


public class CrawlActionFactoryImpl implements CrawlActionFactory {

    private Instant deadline;
    private Map<String, Integer> counts;
    private Set<String> visitedUrls;

    private Clock clock;

    private List<Pattern> ignoredUrls;

    private int maxDepth;

    private PageParserFactory pageParserFactory;

    public CrawlActionFactoryImpl(Instant deadline,
                                  Clock clock, int maxDepth, List<Pattern> ignoredUrls,
                                  Map<String, Integer> counts, Set<String> visitedUrls,
                                  PageParserFactory pageParserFactory) {
        this.deadline = deadline;
        this.clock = clock;
        this.maxDepth = maxDepth;
        this.ignoredUrls = ignoredUrls;
        this.counts = counts;
        this.visitedUrls = visitedUrls;
        this.pageParserFactory = pageParserFactory;
    }

    public CrawlAction get(String url) {
        CrawlAction.Builder crawlBuild = new CrawlAction.Builder();
        crawlBuild.setDeadline(deadline);
        crawlBuild.setMaxDepth(maxDepth);
        crawlBuild.setIgnoredUrls(ignoredUrls);
        crawlBuild.setClock(clock);
        crawlBuild.setUrl(url);
        crawlBuild.setCountsBuild(counts);
        crawlBuild.setVisitedUrls(visitedUrls);
        crawlBuild.setParserFactory(pageParserFactory);

        return crawlBuild.build();
    }

}
