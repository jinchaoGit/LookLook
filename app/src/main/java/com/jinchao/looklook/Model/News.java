package com.jinchao.looklook.Model;

/**
 * Created by ljc on 18-5-23.
 */

public class News {
    private String news_title;
    private String news_summary;
    private String news_imageUrl;
    private String news_time;
    private String news_urls;

    public String getNews_urls() {
        return news_urls;
    }

    public void setNews_urls(String news_urls) {
        this.news_urls = news_urls;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_summary() {
        return news_summary;
    }

    public void setNews_summary(String news_summary) {
        this.news_summary = news_summary;
    }

    public String getNews_imageUrl() {
        return news_imageUrl;
    }

    public void setNews_imageUrl(String news_imageUrl) {
        this.news_imageUrl = news_imageUrl;
    }

    public String getNews_time() {
        return news_time;
    }

    public void setNews_time(String news_time) {
        this.news_time = news_time;
    }

}
