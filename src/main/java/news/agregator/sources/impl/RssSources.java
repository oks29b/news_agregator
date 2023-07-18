package news.agregator.sources.impl;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.agregator.entity.News;
import news.agregator.entity.RssEntity;
import news.agregator.repository.RssEntityRepository;
import news.agregator.sources.Sources;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RssSources implements Sources {
    private final RssEntityRepository rssEntityRepository;

    @Override
    @Async
    public CompletableFuture<List<News>> getSources() {
        /**
         *  Получаем адреса rss из db
         */
        Map<String, String> sources = findSources();

        // Создаем CompletableFuture для каждого URL и запускаем их асинхронное чтение
        List<CompletableFuture<News>> futureList = sources.values().stream()
                .map(this::readNews)
                .collect(Collectors.toList());
        // Комбинируем все CompletableFuture и возвращаем список прочитанных новостей
        return CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]))
                .thenApply(v -> futureList.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<News> readNews(String rss) {
        News news = new News();
        log.info(LocalDateTime.now() + " started finding news from url = " + rss);
        SyndFeed syndFeed = feedFromUrl(rss);
//            if(syndFeed == null) continue;
        List<SyndEntry> entries = (List<SyndEntry>) syndFeed.getEntries();
        for(SyndEntry syndEntry : entries){
             news = mapEntryToEntity(syndEntry);
        }
        log.info(LocalDateTime.now() + " completed finding news from url = " + rss);

        return CompletableFuture.completedFuture(news);
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

    private News mapEntryToEntity(SyndEntry syndEntry){
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
