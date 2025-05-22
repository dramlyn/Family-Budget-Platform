package fbp.app.service;

import fbp.app.dto.budget_period.BudgetPeriodDto;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.BudgetPeriodMapper;
import fbp.app.model.BudgetPeriod;
import fbp.app.repository.BudgetPeriodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetPeriodService {
    private final BudgetPeriodRepository budgetPeriodRepository;

    public BudgetPeriodDto getBudgetPeriodById(Long budgetPeriodId) {
        BudgetPeriod budgetPeriod = budgetPeriodRepository
                .findById(budgetPeriodId)
                .orElseThrow(() -> new UserServiceException("Budget period with id %s not found".formatted(budgetPeriodId), HttpStatus.NOT_FOUND));

        return BudgetPeriodMapper.toBudgetPeriodDto(budgetPeriod);
    }
}
