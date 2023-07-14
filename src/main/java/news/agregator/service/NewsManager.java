package news.agregator.service;

import news.agregator.entity.News;
import news.agregator.sources.Sources;

import java.util.List;

public interface NewsManager {
    List<News> findAllNews();
}
