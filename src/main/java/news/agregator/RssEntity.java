package news.agregator;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class RssEntity {

    @Id
    private Integer id;
    private String name;
    private String link;
}
