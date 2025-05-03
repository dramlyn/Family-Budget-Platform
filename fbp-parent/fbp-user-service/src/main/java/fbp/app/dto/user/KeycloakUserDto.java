package fbp.app.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KeycloakUserDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
