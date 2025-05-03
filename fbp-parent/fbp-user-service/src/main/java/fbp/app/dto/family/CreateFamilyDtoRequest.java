package fbp.app.dto.family;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateFamilyDtoRequest {
    @NotNull
    @NotEmpty
    private String name;
    private String description;
}
