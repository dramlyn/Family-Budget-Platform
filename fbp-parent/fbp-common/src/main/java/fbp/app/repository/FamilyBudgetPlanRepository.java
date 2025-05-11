package fbp.app.repository;

import fbp.app.model.FamilyBudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyBudgetPlanRepository extends JpaRepository<FamilyBudgetPlan, Long> {
    List<FamilyBudgetPlan> findByFamilyId(Long familyId);
}
