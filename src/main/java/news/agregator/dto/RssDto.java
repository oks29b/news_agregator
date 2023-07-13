package news.agregator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RssDto {
    private String name;
    private String link;
}
