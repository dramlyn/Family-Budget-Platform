package fbp.app.dto.user;

import fbp.app.model.type.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoDto {
    private String firstName;
    private String lastName;
    private Long userId;
    private Role role;
}
