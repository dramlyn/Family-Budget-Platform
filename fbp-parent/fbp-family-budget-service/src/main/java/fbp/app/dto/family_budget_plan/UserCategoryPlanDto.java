package fbp.app.dto.family_budget_plan;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserCategoryPlanDto {
    private Long id;
    private Long categoryId;
    private Long userId;
    private Integer limit;
    private Long periodId;
}
