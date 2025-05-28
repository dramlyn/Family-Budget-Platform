package fbp.app.rest;

import fbp.app.config.KeycloakActions;
import fbp.app.dto.user.*;
import fbp.app.model.type.Role;
import fbp.app.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/parent")
    public ResponseEntity<UserDto> registerParent(@RequestBody @Valid RegisterParentDtoRequest request) {
        return ResponseEntity.ok(userService.registerParent(request, Role.PARENT));
    }

    @PreAuthorize("hasRole('ROLE_PARENT')")
    @PostMapping("/user")
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid RegisterUserRequestDto request) {
        return ResponseEntity.ok(userService.registerUser(request, Role.USER));
    }

    @PreAuthorize("hasRole('ROLE_PARENT')")
    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<Void> forgotPassword(@PathVariable String email) {
        userService.sendKeycloakRequiredActionsEmail(email, KeycloakActions.UPDATE_PASSWORD);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordDtoRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserDto> updateUserInfo(@RequestBody UpdateUserDtoRequest request,
                                                  @PathVariable String email) {
        return ResponseEntity.ok(userService.updateUser(request, email));
    }

    @GetMapping("/whoami")
    public ResponseEntity<UserDto> whoami(){
        return ResponseEntity.ok(userService.getUserByKkId(SecurityContextHolder.getContext().getAuthentication().getName()));
    }
}
