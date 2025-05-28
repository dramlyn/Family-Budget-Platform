package fbp.app.service;

import fbp.app.dto.budget_period.BudgetPeriodDto;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.BudgetPeriodMapper;
import fbp.app.model.BudgetPeriod;
import fbp.app.repository.BudgetPeriodRepository;
import fbp.app.util.BudgetPeriodUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetPeriodService {
    private final BudgetPeriodRepository budgetPeriodRepository;
    private final BudgetPeriodUtil budgetPeriodUtil;

    public BudgetPeriodDto getBudgetPeriodById(Long budgetPeriodId) {
        BudgetPeriod budgetPeriod = budgetPeriodRepository
                .findById(budgetPeriodId)
                .orElseThrow(() -> new UserServiceException("Budget period with id %s not found".formatted(budgetPeriodId), HttpStatus.NOT_FOUND));

        return BudgetPeriodMapper.toBudgetPeriodDto(budgetPeriod);
    }

    public BudgetPeriodDto getCurrentBudgetPeriod() {
        BudgetPeriod budgetPeriod = budgetPeriodUtil.getOrCreateCurrentBudgetPeriod();

        return BudgetPeriodMapper.toBudgetPeriodDto(budgetPeriod);
    }

    public BudgetPeriodDto getBudgetPeriodByYearAndMonth(int year, int month) {
        BudgetPeriod budgetPeriod = budgetPeriodRepository.findByYearAndMonth(year, month)
                .orElseThrow(() -> new UserServiceException("Budget period with year %d and month %d not found".formatted(year, month), HttpStatus.NOT_FOUND));

        return BudgetPeriodMapper.toBudgetPeriodDto(budgetPeriod);
    }
}
