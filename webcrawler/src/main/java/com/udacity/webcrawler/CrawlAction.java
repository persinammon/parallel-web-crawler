package com.udacity.webcrawler;

import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;

import java.util.ArrayList;


public final class CrawlAction extends RecursiveAction {
    private final String url;
    private final Instant deadline;

    private Map<String, Integer> counts;

    private Set<String> visitedUrls;

    private final Clock clock;

    private final List<Pattern>  ignoredUrls;

    private final int maxDepth;

    @Inject
    private PageParserFactory parserFactory;


    /**
     * Private constructor so can use the Builder pattern.
     */
    private CrawlAction(String url, Instant deadline,
                        Clock clock, List<Pattern> ignoredUrls, int maxDepth) {
        this.url = url;
        this.deadline = deadline;
        this.clock = clock;
        this.ignoredUrls = ignoredUrls;
        this.maxDepth = maxDepth;
    }
    @Override
    protected void compute() {
        System.out.println("compute reached");

        if (clock.instant().isAfter(deadline)) {
            return;
        }
        for (Pattern pattern : ignoredUrls) {
            if (pattern.matcher(url).matches()) {
                return;
            }
        }

        //not thread-safe
        if (ParallelWebCrawler.visitedUrls.contains(url)) {
            System.out.println("visited urls fired");
            return;
        }
        ParallelWebCrawler.visitedUrls.add(url);
        System.out.println("BREAKPOINT? visited urls " + ParallelWebCrawler.visitedUrls.toString());
        System.out.println("Refr to visitted urls" + visitedUrls);

        System.out.println("Parser factory: " + parserFactory.toString());
        PageParser.Result result = parserFactory.get(url).parse();
        for (Map.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
            ParallelWebCrawler.counts.compute(e.getKey(), (k, v) -> (v == null) ? e.getValue() : v + e.getValue());
        }
        List<CrawlAction> newActions = new ArrayList<CrawlAction>();
        System.out.println("counts " + counts.toString());
        for (String link : result.getLinks()) {
            newActions.add(new CrawlAction(link, this.deadline,
                                        this.clock, this.ignoredUrls, this.maxDepth));
        }
        invokeAll(newActions);
    }

    public static final class Builder {
        private String urlBuild;
        private Instant deadlineBuild;

        private Map<String, Integer> countsBuild;

        private Set<String> visitedUrlsBuild;

        private Clock clockBuild;

        private List<Pattern> ignoredUrlsBuild;

        private int maxDepthBuild;

        public Builder setUrl(String url) {
            this.urlBuild = url;
            return this;
        }
        public Builder setDeadline(Instant deadline)  {
            this.deadlineBuild = deadline;
            return this;
        }



        public Builder setClock(Clock clock) {
            this.clockBuild = clock;
            return this;
        }

        public Builder setIgnoredUrls(List<Pattern> ignoredUrls) {
            this.ignoredUrlsBuild = ignoredUrls;
            return this;
        }

        public Builder setMaxDepth(int maxDepth) {
            this.maxDepthBuild = maxDepth;
            return this;
        }


        public CrawlAction build() {
            return new CrawlAction(urlBuild, deadlineBuild,
                    clockBuild, ignoredUrlsBuild, maxDepthBuild);
        }
    }
}
