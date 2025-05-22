package fbp.app.mapper;

import fbp.app.dto.budget_period.BudgetPeriodDto;
import fbp.app.model.BudgetPeriod;

public class BudgetPeriodMapper {
    public static BudgetPeriodDto toBudgetPeriodDto(BudgetPeriod budgetPeriod) {
        return BudgetPeriodDto.builder()
                .id(budgetPeriod.getId())
                .year(budgetPeriod.getYear())
                .month(budgetPeriod.getMonth())
                .build();
    }
}
