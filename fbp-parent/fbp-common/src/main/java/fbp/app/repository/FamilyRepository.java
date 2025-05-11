package fbp.app.repository;

import fbp.app.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {
}
