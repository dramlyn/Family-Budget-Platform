package fbp.app.rest;

import fbp.app.dto.budget_period.BudgetPeriodDto;
import fbp.app.service.BudgetPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/budget-period")
@RequiredArgsConstructor
public class BudgetPeriodController {
    private final BudgetPeriodService budgetPeriodService;

    @GetMapping("/{periodId}")
    public BudgetPeriodDto getBudgetPeriod(@PathVariable Long periodId) {
        return budgetPeriodService.getBudgetPeriodById(periodId);
    }
}
