package news.agregator.service;

import news.agregator.dto.RssDto;
import news.agregator.entity.RssEntity;

import java.util.List;

public interface RssManager {
    List<RssEntity> findAllRss();
    RssEntity addRss(RssDto rssDto);
    RssEntity removeRss(RssDto rssDto);
}
