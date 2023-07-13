package news.agregator.mapper;

import news.agregator.dto.RssDto;
import news.agregator.entity.RssEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RssMapper {
    RssEntity toRssEntity(RssDto rssDto);
    RssDto toRssDto(RssEntity rssEntity);
    List<RssDto> toListRssDto(List<RssEntity> rssEntityList);
}
