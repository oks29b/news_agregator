package news.agregator.service;

import news.agregator.entity.News;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NewsFetcher {
    List<News> getNews();
    CompletableFuture<List<News>> readNews(String rss);
}
