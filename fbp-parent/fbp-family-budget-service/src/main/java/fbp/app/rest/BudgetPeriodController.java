package fbp.app.rest;

import fbp.app.dto.budget_period.BudgetPeriodDto;
import fbp.app.service.BudgetPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/budget-period")
@RequiredArgsConstructor
public class BudgetPeriodController {
    private final BudgetPeriodService budgetPeriodService;

    @GetMapping("/{periodId}")
    public ResponseEntity<BudgetPeriodDto> getBudgetPeriod(@PathVariable Long periodId) {
        return ResponseEntity.ok(budgetPeriodService.getBudgetPeriodById(periodId));
    }

    @GetMapping
    public ResponseEntity<BudgetPeriodDto> getCurrentBudgetPeriod(){
        return ResponseEntity.ok(budgetPeriodService.getCurrentBudgetPeriod());
    }

    @GetMapping("/by-year-and-month")
    public ResponseEntity<BudgetPeriodDto> getBudgetPeriodByYearAndMonth(
            @RequestParam int year,
            @RequestParam int month
    ){
        return ResponseEntity.ok(budgetPeriodService.getBudgetPeriodByYearAndMonth(year, month));
    }
}
