package fbp.app.dto.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCategoryDtoRequest {
    @NotEmpty
    @NotNull
    private String name;
    private String description;
}
