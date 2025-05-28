package fbp.app.dto.user_budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SumUserBudgetDto {
    private Long userId;
    private Long periodId;
    private Integer budget;
}
