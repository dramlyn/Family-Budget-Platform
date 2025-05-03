package fbp.app.rest;

import fbp.app.config.KeycloakActions;
import fbp.app.dto.user.RegisterParentDtoRequest;
import fbp.app.dto.user.RegisterUserRequestDto;
import fbp.app.dto.user.Role;
import fbp.app.model.User;
import fbp.app.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/parent")
    public ResponseEntity<User> registerParent(@RequestBody @Valid RegisterParentDtoRequest request) {
        return ResponseEntity.ok(userService.registerParent(request, Role.PARENT));
    }

    @PreAuthorize("hasRole('ROLE_PARENT')")
    @PostMapping("/user")
    public ResponseEntity<User> registerUser(@RequestBody @Valid RegisterUserRequestDto request) {
        return ResponseEntity.ok(userService.registerUser(request, Role.USER));
    }

    @PreAuthorize("hasRole('ROLE_PARENT')")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<Void> forgotPassword(@PathVariable String email) {
        userService.sendKeycloakRequiredActionsEmail(email, KeycloakActions.UPDATE_PASSWORD);
        return ResponseEntity.ok().build();
    }
}
