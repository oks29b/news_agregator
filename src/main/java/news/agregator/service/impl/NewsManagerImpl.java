package news.agregator.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.agregator.entity.News;
import news.agregator.repository.NewsRepository;
import news.agregator.service.NewsFetcher;
import news.agregator.service.NewsManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class NewsManagerImpl implements NewsManager {
    private final NewsFetcher newsFetcher;
    private final NewsRepository newsRepository;

    @Override
    @Async
    @Scheduled(fixedRateString = "${schedule.interval.period}")
    public void findAllNews() {
        log.info(LocalDateTime.now() + " started finding news, at this time count of news to elasticsearch = " + newsRepository.count());

        List<News> news = newsFetcher.getNews().stream()
                .filter(Objects::nonNull)
                .toList();

        newsRepository.saveAll(news);

        log.info(LocalDateTime.now() + " was saved news, at this time count of news in elasticsearch = " + newsRepository.count());
    }
}
