package news.agregator.repository;

import news.agregator.entity.News;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends ElasticsearchRepository<News, String> {
    List<News> findByTitle(String title);
}
