package news.agregator.service;

import news.agregator.dto.NewsDto;

import java.util.List;

public interface NewsManager {
    List<NewsDto> findAllNews();
}
