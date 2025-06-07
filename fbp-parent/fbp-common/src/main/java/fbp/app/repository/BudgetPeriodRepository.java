package fbp.app.repository;

import fbp.app.model.BudgetPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetPeriodRepository extends JpaRepository<BudgetPeriod, Long> {
    Optional<BudgetPeriod> findByYearAndMonth(int year, int month);
}
