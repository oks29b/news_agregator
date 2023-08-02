package news.agregator.service.impl;

import news.agregator.entity.News;
import news.agregator.entity.RssEntity;
import news.agregator.repository.RssEntityRepository;
import news.agregator.service.NewsFetcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RssNewsFetcherTest {

    @Mock
    private RssEntityRepository rssEntityRepository;
    @Spy
    private NewsFetcher newsFetcher;
    @InjectMocks
    private RssNewsFetcher rssNewsFetcher;

    @Test
    public void testGetNews() throws Exception {
        // Mock the response from the newsFetcher.readNews() method
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
        RssEntity rssEntity = RssEntity.builder()
                .name("rss")
                .link("https://example.com/rss")
                .build();

        CompletableFuture<List<News>> futureNewsList = CompletableFuture.completedFuture(newsList);
        when(newsFetcher.readNews("https://example.com/rss")).thenReturn(futureNewsList);
        when(rssEntityRepository.findAll()).thenReturn(List.of(rssEntity));

        // Invoke the getNews() method
        List<News> result = rssNewsFetcher.getNews();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals("news1", result.get(0).getTitle());
        assertEquals("news2", result.get(1).getTitle());
    }
}
