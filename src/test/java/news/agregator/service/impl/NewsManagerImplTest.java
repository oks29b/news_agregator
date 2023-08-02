package news.agregator.service.impl;

import news.agregator.entity.News;
import news.agregator.repository.NewsRepository;
import news.agregator.service.NewsFetcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsManagerImplTest {

    @Mock
    NewsFetcher newsFetcher;
    @Mock
    NewsRepository newsRepository;
    @InjectMocks
    NewsManagerImpl newsManager;
    @Captor
    ArgumentCaptor<List<News>> captor;

    @Test
    public void findAllNews() {
        List<News> newsList = new ArrayList<>();
        newsList.add(News.builder()
                .title("news1")
                .description("d1")
                .localDate(LocalDate.now())
                .link("https://example.com/rss")
                .build());
        newsList.add(News.builder()
                .title("news2")
                .description("d2")
                .localDate(LocalDate.now())
                .link("https://example.com/rss")
                .build());

        when(newsFetcher.getNews()).thenReturn(newsList);
        newsManager.findAllNews();

        verify(newsRepository).saveAll(captor.capture());
        List<News> res = captor.getValue();
        assertEquals(2, res.size());
        assertEquals("news1", res.get(0).getTitle());
        assertEquals("news2", res.get(1).getTitle());
    }
}
