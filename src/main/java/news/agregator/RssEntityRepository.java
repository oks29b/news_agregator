package news.agregator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RssEntityRepository extends JpaRepository<RssEntity, Integer> {
}
