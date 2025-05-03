package fbp.app.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import fbp.app.config.KeycloakActions;
import fbp.app.dto.family.CreateFamilyDtoRequest;
import fbp.app.dto.user.*;
import fbp.app.exception.UserServiceException;
import fbp.app.model.User;
import fbp.app.repository.FamilyRepository;
import fbp.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final Keycloak keycloak;
    private final FamilyService familyService;

    @Value("${keycloak.realm}")
    private String realm;

    @Transactional
    public User registerUser(RegisterUserRequestDto request, Role role) {
        KeycloakUserDto registerUserRequestDto = KeycloakUserDto.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .build();
        String keycloakId = createKeycloakUser(registerUserRequestDto, role);

        User user = fillCommonUserFields(request.getFirstName(),
                request.getLastName(), role, request.getEmail(), keycloakId);
        user.setFamilyId(request.getFamilyId());

        return saveUser(user);
    }

    @Transactional
    public User registerParent(RegisterParentDtoRequest request, Role role) {
        KeycloakUserDto registerUserRequestDto = KeycloakUserDto.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .build();
        String keycloakId = createKeycloakUser(registerUserRequestDto, role);

        User user = fillCommonUserFields(registerUserRequestDto.getFirstName(), registerUserRequestDto.getLastName(),
                role, registerUserRequestDto.getEmail(), keycloakId);

        CreateFamilyDtoRequest createFamilyDtoRequest = new CreateFamilyDtoRequest();
        createFamilyDtoRequest.setName(request.getFamilyName());
        createFamilyDtoRequest.setDescription(request.getFamilyName());

        user.setFamilyId(familyService.createFamily(createFamilyDtoRequest).getId());

        return saveUser(user);
    }

    @Transactional
    public User addParentToFamily(AddParentToFamilyDtoRequest request, Long familyId) {
        if (familyRepository.findById(familyId).isEmpty()) {
            String message = String.format("Family with specified id %s not found.", familyId);
            log.error(message);
            throw new UserServiceException(message, HttpStatus.BAD_REQUEST);
        }

        KeycloakUserDto registerUserRequestDto = KeycloakUserDto.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .build();

        String keycloakId = createKeycloakUser(registerUserRequestDto, Role.PARENT);

        User user = fillCommonUserFields(request.getFirstName(),
                request.getLastName(), Role.PARENT, request.getEmail(), keycloakId);
        user.setFamilyId(familyId);

        return saveUser(user);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete from Keycloak
        deleteKeycloakUser(user.getKeycloakId());

        // Delete from database
        userRepository.deleteById(user.getId());
    }

    public void sendKeycloakRequiredActionsEmail(String email, KeycloakActions kkAction) {
        List<UserRepresentation> users = keycloak
                .realm(realm)
                .users()
                .search(email);

        if (users.isEmpty()) {
            String message = String.format("User with email %s not found", email);
            log.error(message);
            throw new UserServiceException(message, HttpStatus.BAD_REQUEST);
        }

        UserRepresentation user = users.get(0);
        try {
            // Инициируйте отправку email сброса
            keycloak
                    .realm(realm)
                    .users()
                    .get(user.getId())
                    .executeActionsEmail(Collections.singletonList(kkAction.name()));
            log.info("Message sent.");
        } catch (Exception e) {
            log.warn("error sending keycloak required actions email", e);
            throw new UserServiceException(String.format("Failed to send reset email: %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User fillCommonUserFields(String firstName, String lastName, Role role, String email, String kkId) {
        User user = new User();
        user.setEmail(email);
        user.setKeycloakId(kkId);
        user.setRole(role);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCreatedAt(Instant.now());
        return user;
    }

    private String createKeycloakUser(KeycloakUserDto userDto, Role role) {
        if(!keycloak.realm(realm).users().search(userDto.getEmail()).isEmpty()){
            throw new UserServiceException(String.format("User with email %s already exists in kk.", userDto.getEmail()),
                    HttpStatus.BAD_REQUEST);
        }

        UserRepresentation user = getUserRepresentation(userDto, role);

        try {
            jakarta.ws.rs.core.Response response = keycloak.realm(realm).users().create(user);
            String kkId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            sendEmailVerification(kkId, userDto.getEmail());
            return kkId;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserServiceException(String.format("Failed to create keycloak user: %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendEmailVerification(String kkId, String email) {
        log.info("Verifying email for user {}", email);

        final UserResource userResource = keycloak.realm(realm).users().get(kkId);
        try {
            executeActionsEmail(userResource, List.of("VERIFY_EMAIL"));
        } catch (Exception e) {
            String message = String.format("failed verify email: %s, cause = %s", email, e.getMessage());
            log.error(message);
            throw new UserServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void executeActionsEmail(UserResource userResource, List<String> actions) {
        userResource.executeActionsEmail(
                actions
        );
    }

    private static UserRepresentation getUserRepresentation(KeycloakUserDto userDto, Role role) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDto.getEmail());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRealmRoles(Collections.singletonList(role.name()));
        user.setEmailVerified(false);
        user.setRequiredActions(List.of(KeycloakActions.UPDATE_PASSWORD.name()));

        /*// Set password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.getPassword());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));*/
        return user;
    }

    private User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            deleteKeycloakUser(user.getKeycloakId());

            String message = String.format("Failed to create user at family with specified id %s. Exc message: %s", user.getFamilyId(), e.getMessage());
            log.error(message);
            throw new UserServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void deleteKeycloakUser(String kkId) {
        keycloak.realm(realm).users().delete(kkId);
    }
}
