package news.agregator.repository;

import news.agregator.entity.RssEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RssEntityRepository extends JpaRepository<RssEntity, Integer> {
}
