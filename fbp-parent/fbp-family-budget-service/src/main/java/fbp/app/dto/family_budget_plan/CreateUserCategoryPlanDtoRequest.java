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
public class CreateUserCategoryPlanDtoRequest {
    @NotNull
    private Long categoryId;
    @NotNull
    private Long userId;
    @NotNull
    private Integer limit;
}
