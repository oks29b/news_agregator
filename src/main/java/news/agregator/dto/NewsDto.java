package news.agregator.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NewsDto {
    private String title;
    private String description;
    private LocalDate localDate;
    private String link;
}
