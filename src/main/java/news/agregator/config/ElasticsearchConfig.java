package news.agregator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "news/agregator")
@ComponentScan(basePackages = "news/agregator")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String ELASTICSEARCH_URL;

    @Value("${elasticsearch.sockettimeout}")
    private String ELASTICSEARCH_SOCKETTIMEOUT;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(ELASTICSEARCH_URL)
                .withSocketTimeout(Long.parseLong(ELASTICSEARCH_SOCKETTIMEOUT))
                .build();
    }
}
