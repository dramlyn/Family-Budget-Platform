package fbp.app.dto.family_budget_plan;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateFamilyBudgetPlanDto {
    @NotNull(message = "Поле familyId н может быть null")
    private Long familyId;
    @NotNull(message = "Поле budgetLimit н может быть null")
    private Integer budgetLimit;
    @Valid
    @NotEmpty
    @NotNull
    private List<CreateUserCategoryPlanDtoRequest> familyMembersPlan;
}
