package news.agregator.sources;

import news.agregator.entity.News;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Sources {
    CompletableFuture<List<News>> getSources();
}
