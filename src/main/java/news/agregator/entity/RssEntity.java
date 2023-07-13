package news.agregator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "rss")
@NoArgsConstructor
public class RssEntity {

    @Id
    private Integer id;
    private String name;
    private String link;
}
