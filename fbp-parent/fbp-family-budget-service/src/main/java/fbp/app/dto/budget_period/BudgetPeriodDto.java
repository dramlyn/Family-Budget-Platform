package fbp.app.dto.budget_period;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BudgetPeriodDto {
    private Long id;
    private int year;
    private int month;
}
