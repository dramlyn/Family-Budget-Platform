package fbp.app.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDtoRequest {
    @NotNull(message = "Поле email не должно быть null")
    @NotEmpty(message = "Поле email не должно быть пустым")
    private String email;
    @NotNull(message = "Поле oldPassword не должно быть null")
    @NotEmpty(message = "Поле oldPassword не должно быть пустым")
    private String oldPassword;
    @NotNull(message = "Поле newPassword не должно быть null")
    @NotEmpty(message = "Поле newPassword не должно быть пустым")
    private String newPassword;
}
