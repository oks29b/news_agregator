package news.agregator;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class NewsDto {
    private String title;
    private String description;
    private Date localDate;
    private String link;
}
