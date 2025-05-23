package fbp.app.repository;

import fbp.app.model.FamilyBudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyBudgetPlanRepository extends JpaRepository<FamilyBudgetPlan, Long> {
    List<FamilyBudgetPlan> findByFamilyId(Long familyId);
    Optional<FamilyBudgetPlan> findByFamilyIdAndPeriodId(Long familyId, Long periodId);
}
