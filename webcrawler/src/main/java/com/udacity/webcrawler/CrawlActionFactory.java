package com.udacity.webcrawler;

public interface CrawlActionFactory {
    CrawlAction get(String url);
}
