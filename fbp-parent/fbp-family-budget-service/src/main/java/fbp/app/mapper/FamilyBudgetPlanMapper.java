package fbp.app.mapper;

import fbp.app.dto.family_budget_plan.CreateFamilyBudgetPlanDto;
import fbp.app.dto.family_budget_plan.FamilyBudgetPlanDto;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.Family;
import fbp.app.model.FamilyBudgetPlan;

public class FamilyBudgetPlanMapper {
    public static FamilyBudgetPlan toModel(CreateFamilyBudgetPlanDto dto, Family family, BudgetPeriod budgetPeriod) {
        return FamilyBudgetPlan.builder()
                .family(family)
                .period(budgetPeriod)
                .limit(dto.getBudgetLimit())
                .build();
    }

    public static FamilyBudgetPlanDto toDto(FamilyBudgetPlan familyBudgetPlan){
        return FamilyBudgetPlanDto.builder()
                .id(familyBudgetPlan.getId())
                .familyBudgetLimit(familyBudgetPlan.getLimit())
                .periodId(familyBudgetPlan.getPeriod().getId())
                .familyId(familyBudgetPlan.getFamily().getId())
                .build();
    }
}
