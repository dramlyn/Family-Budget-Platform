package fbp.app.dto.goal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopUpGoalBalanceDtoRequest {
    @NotNull
    private Long goalId;
    @NotNull
    private Integer balance;
}
