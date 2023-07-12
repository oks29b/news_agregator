package news.agregator.repository;

import news.agregator.entity.News;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NewsRepository extends ElasticsearchRepository<News, String> {
}
