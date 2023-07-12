package news.agregator.service.impl;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import lombok.AllArgsConstructor;
import news.agregator.dto.NewsDto;
import news.agregator.dto.RssDto;
import news.agregator.mapper.RssMapper;
import news.agregator.repository.NewsRepository;
import news.agregator.repository.RssEntityRepository;
import news.agregator.service.NewsManager;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class NewsManagerImpl implements NewsManager {
    private final RssEntityRepository rssEntityRepository;
    private final RssMapper rssMapper;
    private final NewsRepository newsRepository;

    @Override
    @Async
    @Scheduled(fixedRateString = "${schedule.interval.period}")
    public List<NewsDto> findAllNews() {
        /**
         *  Получаем адреса rss из db
         */

        Map<String, String> sources = getSources();
        List<NewsDto> newsDtos = new ArrayList<>();

        for(String rss : sources.values()){
            SyndFeed syndFeed = feedFromUrl(rss);
            if(syndFeed == null) continue;
            List<SyndEntry> entries = (List<SyndEntry>) syndFeed.getEntries();
            for(SyndEntry syndEntry : entries){
                NewsDto newsDto = mapEntryToDto(syndEntry);
                newsDtos.add(newsDto);
            }
        }
        newsRepository.saveAll();
        System.out.println(newsDtos);
        System.out.println("________________" + LocalDateTime.now());
        return newsDtos;
    }

    private NewsDto mapEntryToDto(SyndEntry syndEntry){
        return NewsDto.builder()
                .title(syndEntry.getTitle())
                .description(syndEntry.getDescription() == null ? "" : syndEntry.getDescription().getValue())
                .localDate(syndEntry.getPublishedDate())
                .link(syndEntry.getLink())
                .build();
    }

    /**
     * Считывает из db адреса rss и возвращает их как значения Map
     */
    private Map<String, String> getSources() {
        List<RssDto> listRss = rssMapper.toListRssDto(rssEntityRepository.findAll());
        Map<String, String> sources = new HashMap<>();
        for(RssDto rssDto: listRss) {
            sources.put(rssDto.getName(), rssDto.getLink());
        }
        return sources;
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
            System.out.println("failed to connect - " + url + " skipped");
            return null;
        } catch (FeedException | NullPointerException e) {
            System.out.println("failed to parse response from - " + url + " skipped");
            return null;
        }
    }
}
