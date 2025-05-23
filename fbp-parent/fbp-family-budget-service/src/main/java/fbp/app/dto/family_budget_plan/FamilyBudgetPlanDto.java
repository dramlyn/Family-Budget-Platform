package fbp.app.dto.family_budget_plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FamilyBudgetPlanDto {
    private Long id;
    private Long familyId;
    private int familyBudgetLimit;
    private Long periodId;
}
