package fbp.app.util;

import fbp.app.exception.UserServiceException;
import fbp.app.model.BudgetPeriod;
import fbp.app.repository.BudgetPeriodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BudgetPeriodUtil {
    private final BudgetPeriodRepository budgetPeriodRepository;

    public BudgetPeriod getOrCreateBudgetPeriod() {
        LocalDateTime localDateTime = LocalDateTime.now();

        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();

        try {
            Optional<BudgetPeriod> budgetPeriodOptional = budgetPeriodRepository.findByYearAndMonth(year, month);

            if(budgetPeriodOptional.isPresent()) {
                return budgetPeriodOptional.get();
            } else {
                BudgetPeriod budgetPeriod = BudgetPeriod.builder().year(year).month(month).build();
                return budgetPeriodRepository.save(budgetPeriod);
            }
        } catch (RuntimeException e) {
            String message = String.format(e.getMessage());
            log.error(message);
            throw new UserServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
