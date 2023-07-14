package news.agregator.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.agregator.entity.News;
import news.agregator.repository.NewsRepository;
import news.agregator.service.NewsManager;
import news.agregator.sources.Sources;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class NewsManagerImpl implements NewsManager {
    private final Sources sources;
    private final NewsRepository newsRepository;
    @Override
    @Scheduled(fixedRateString = "${schedule.interval.period}")
    public List<News> findAllNews(){
        log.info(LocalDateTime.now() + " started finding news, at this time count of news to elasticsearch = " + newsRepository.count());
        CompletableFuture<List<News>> listCompletableFuture = sources.getSources();
        List<News> news = new ArrayList<>();
        try{
            news = (List<News>)newsRepository.saveAll(
                    listCompletableFuture.get().stream().toList());
        }catch (Throwable e){
            System.out.println(e.getCause());
        }

        log.info(LocalDateTime.now() + " was saved news, at this time count of news in elasticsearch = " + newsRepository.count());
        return news;
    }
}
