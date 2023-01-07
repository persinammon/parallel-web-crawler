package com.udacity.webcrawler;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.time.Instant;
import java.util.regex.Pattern;


public class CrawlActionFactoryImpl implements CrawlActionFactory {

    private Instant deadline;
    //private Map<String, Integer> counts;
    //private Set<String> visitedUrls;

    private Clock clock;

    private List<Pattern> ignoredUrls;

    private int maxDepth;

    public CrawlActionFactoryImpl(Instant deadline,
                                  Clock clock, int maxDepth, List<Pattern> ignoredUrls) {
        this.deadline = deadline;
        this.clock = clock;
        this.maxDepth = maxDepth;
        this.ignoredUrls = ignoredUrls;
    }

    public CrawlAction get(String url) {
        CrawlAction.Builder crawlBuild = new CrawlAction.Builder();
        crawlBuild.setDeadline(deadline);
        crawlBuild.setMaxDepth(maxDepth);
        crawlBuild.setIgnoredUrls(ignoredUrls);
        crawlBuild.setClock(clock);
        crawlBuild.setUrl(url);

        return crawlBuild.build();
    }

}
