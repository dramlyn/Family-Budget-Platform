package fbp.app.repository;

import fbp.app.model.Family;
import fbp.app.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByFamilyId(Long familyId);
}
