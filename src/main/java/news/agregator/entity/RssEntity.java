package news.agregator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(name = "rss")
@NoArgsConstructor
@AllArgsConstructor
public class RssEntity {

    @Id
    private Integer id;
    private String name;
    private String link;
}
