package news.agregator.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
