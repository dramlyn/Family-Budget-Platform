package fbp.app.dto.goal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateGoalDtoRequest {
    @NotNull(message = "Поле cost не может быть null")
    private Integer cost;
    @NotNull(message = "Поле name не может быть null")
    @NotEmpty(message = "Поле name не может быть пустым")
    private String name;
    private String description;
    @NotNull(message = "Поле familyId не может быть null")
    private Long familyId;
}
