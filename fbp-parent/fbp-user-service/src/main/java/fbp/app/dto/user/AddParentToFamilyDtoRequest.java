package fbp.app.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddParentToFamilyDtoRequest {
    @NotEmpty(message = "Поле email не должно быть пустым")
    @NotNull(message = "Поле email не должно быть null")
    private String email;
    @NotEmpty(message = "Поле password не должно быть пустым")
    @NotNull(message = "Поле password не должно быть null")
    private String password;
    @NotEmpty(message = "Поле firstName не должно быть пустым")
    @NotNull(message = "Поле firstName не должно быть null")
    private String firstName;
    @NotEmpty(message = "Поле lastName не должно быть пустым")
    @NotNull(message = "Поле lastName не должно быть null")
    private String lastName;
}
