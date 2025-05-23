package fbp.app.dto.mandatory_payment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMandatoryPaymentDtoRequest {
    @NotNull(message = "Поле name не может быть null")
    @NotEmpty(message = "Поле name не может быть пустым")
    private String name;
    @NotNull(message = "Поле amount не может быть null")
    private Integer amount;
    @NotNull(message = "Поле familyId не может быть null")
    private Long familyId;
}
