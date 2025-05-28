package fbp.app.repository;

import fbp.app.model.UserCategoryPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCategoryPlanRepository extends JpaRepository<UserCategoryPlan, Long> {
    List<UserCategoryPlan> findByUserId(Long userId);
    List<UserCategoryPlan> findByCategoryId(Long categoryId);
    @Query("SELECT u FROM UserCategoryPlan u " +
            "WHERE u.period.id = :periodId " +
            "AND u.user.family.id = :familyId")
    List<UserCategoryPlan> findByFamilyIdAndPeriodId(Long periodId, Long familyId);
    List<UserCategoryPlan> findByUserIdAndPeriodId(Long userId, Long periodId);
    Optional<UserCategoryPlan> findByCategoryIdAndPeriodIdAndUserId(Long categoryId, Long periodId, Long userId);
}
