package news.agregator.service.impl;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import news.agregator.entity.News;
import news.agregator.entity.RssEntity;
import news.agregator.repository.RssEntityRepository;
import news.agregator.service.NewsFetcher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RssNewsFetcher implements NewsFetcher {
    private final RssEntityRepository rssEntityRepository;
    private final NewsFetcher newsFetcher;

    public RssNewsFetcher(RssEntityRepository rssEntityRepository, @Lazy NewsFetcher newsFetcher) {
        this.rssEntityRepository = rssEntityRepository;
        this.newsFetcher = newsFetcher;
    }

    @Override
    public List<News> getNews() {
        Map<String, String> rss = findSources();
        List<CompletableFuture<List<News>>> futureList = rss.values().stream()
                .map(newsFetcher::readNews)
                .toList();
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

        List<News> list = new ArrayList<>();
        for (CompletableFuture<List<News>> listCompletableFuture : futureList) {
            List<News> news;
            try {
                news = listCompletableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                continue;
            }
            if (news != null) {
                list.addAll(news);
            }
        }
        return list;
    }

    @Async
    @Override
    public CompletableFuture<List<News>> readNews(String rss) {
        long start = System.currentTimeMillis();
        log.info(LocalDateTime.now() + " started finding news from url = " + rss);

        List<News> newsList = new ArrayList<>();
        SyndFeed syndFeed = feedFromUrl(rss);
        if (syndFeed == null) return null;
        List<SyndEntry> entries = (List<SyndEntry>) syndFeed.getEntries();
        for (SyndEntry syndEntry : entries) {
            newsList.add(mapEntryToEntity(syndEntry));
        }
        long diffTime = System.currentTimeMillis() - start;
        log.info("completed in {} ms finding news from url = {}", diffTime, rss);

        return CompletableFuture.completedFuture(newsList);
    }

    /**
     * Считывает из db адреса rss и возвращает их как значения Map
     */
    private Map<String, String> findSources() {
        return rssEntityRepository.findAll().stream()
                .collect(Collectors.toMap(
                        RssEntity::getName,
                        RssEntity::getLink));
    }

    private News mapEntryToEntity(SyndEntry syndEntry) {
        return News.builder()
                .title(syndEntry.getTitle())
                .description(syndEntry.getDescription() == null ? "" : syndEntry.getDescription().getValue())
                .localDate(Instant.ofEpochMilli(syndEntry.getPublishedDate().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate())
                .link(syndEntry.getLink())
                .build();
    }

    /**
     * Получает объект SyndFeed загружая rss из переданного url
     *
     * @param url
     * @return SyndFeed - объект из пакета rome
     */
    private SyndFeed feedFromUrl(String url) {
        final int TIMEOUT = 1000;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(TIMEOUT);
            XmlReader reader = new XmlReader(conn); // reader из пакета rome
            return new SyndFeedInput().build(reader);
        } catch (IOException e) {
            log.info("failed to connect - " + url + " skipped");
            return null;
        } catch (FeedException | NullPointerException e) {
            log.info("failed to parse response from - " + url + " skipped");
            return null;
        }
    }
}
