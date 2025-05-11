package fbp.app.dto;

import fbp.app.model.type.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateTransactionDtoRequest {
    @NotNull
    private TransactionType type;
    @NotNull
    private Integer amount;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long userId;
}
