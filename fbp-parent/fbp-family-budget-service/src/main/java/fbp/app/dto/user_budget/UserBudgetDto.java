package fbp.app.dto.user_budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserBudgetDto {
    private Long userId;
    private Long periodId;
    private Long categoryId;
    private Integer budget;
}
