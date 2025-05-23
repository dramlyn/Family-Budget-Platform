package fbp.app.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import fbp.app.config.KeycloakActions;
import fbp.app.dto.family.CreateFamilyDtoRequest;
import fbp.app.dto.user.*;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.UserMapper;
import fbp.app.model.Family;
import fbp.app.model.User;
import fbp.app.model.type.Role;
import fbp.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final Keycloak keycloak;
    private final FamilyService familyService;

    @Value("${keycloak.realm}")
    private String realm;

    @Transactional
    public UserDto registerUser(RegisterUserRequestDto request, Role role) {
        Family family = familyService.findFamilyById(request.getFamilyId());

        KeycloakUserDto registerUserRequestDto = UserMapper.toKeycloakUserDto(request);
        String keycloakId = createKeycloakUser(registerUserRequestDto, role);

        User user = fillCommonUserFields(request.getFirstName(),
                request.getLastName(), role, request.getEmail(), keycloakId);
        user.setFamily(family);

        return UserMapper.toUserDto(saveUser(user));
    }

    @Transactional
    public UserDto registerParent(RegisterParentDtoRequest request, Role role) {
        CreateFamilyDtoRequest createFamilyDtoRequest = new CreateFamilyDtoRequest();
        createFamilyDtoRequest.setName(request.getFamilyName());
        createFamilyDtoRequest.setDescription(request.getFamilyName());
        Family family = familyService.createFamily(createFamilyDtoRequest);

        KeycloakUserDto registerUserRequestDto = UserMapper.toKeycloakUserDto(request);
        String keycloakId = createKeycloakUser(registerUserRequestDto, role);

        User user = fillCommonUserFields(registerUserRequestDto.getFirstName(), registerUserRequestDto.getLastName(),
                role, registerUserRequestDto.getEmail(), keycloakId);

        user.setFamily(family);

        return UserMapper.toUserDto(saveUser(user));
    }

    @Transactional
    public UserDto addParentToFamily(AddParentToFamilyDtoRequest request, Long familyId) {
        Family family = familyService.findFamilyById(familyId);

        KeycloakUserDto registerUserRequestDto = UserMapper.toKeycloakUserDto(request);

        if(userRepository.findByFamilyId(familyId).size() >= 2){
            throw new UserServiceException("In family with id %s is already 2 parents.".formatted(familyId), HttpStatus.CONFLICT);
        }

        String keycloakId = createKeycloakUser(registerUserRequestDto, Role.PARENT);

        User user = fillCommonUserFields(request.getFirstName(),
                request.getLastName(), Role.PARENT, request.getEmail(), keycloakId);
        user.setFamily(family);

        return UserMapper.toUserDto(saveUser(user));
    }

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        deleteKeycloakUser(user.getKeycloakId());

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

    public void changePassword(ChangePasswordDtoRequest request){
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .search(request.getEmail());

        if (users.isEmpty()) {
            throw new UserServiceException("User with email %s not found".formatted(request.getEmail()), HttpStatus.NOT_FOUND);
        }

        //добавить проверку старого пароля

        String userId = users.get(0).getId();

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getNewPassword());
        credential.setTemporary(false);

        try{
            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .resetPassword(credential);
        } catch (Exception e){
            log.error(e.getMessage());
            throw new UserServiceException("Can't change password of user with email %s. Error message: %s".formatted(request.getEmail(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public UserDto updateUser(UpdateUserDtoRequest request, String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserServiceException("User with email %s not found".formatted(email), HttpStatus.NOT_FOUND));
        if(request.getFirstName() != null){
            user.setFirstName(request.getFirstName());
        }
        if(request.getLastName() != null){
            user.setLastName(request.getLastName());
        }

        //добавить измениение имя/фамилия в KK
        return UserMapper.toUserDto(userRepository.save(user));
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
            Response response = keycloak.realm(realm).users().create(user);
            String kkId;
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                kkId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                log.info("User created with ID: {}", kkId);
            } else {
                String error = response.readEntity(String.class);
                log.error("Failed to create user in Keycloak: {}", error);
                throw new UserServiceException("Keycloak user creation failed: " + error, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            sendEmailVerification(kkId, userDto.getEmail());
            return kkId;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserServiceException(String.format("Failed to create keycloak user: %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendEmailVerification(String kkId, String email) {
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

    private UserRepresentation getUserRepresentation(KeycloakUserDto userDto, Role role) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDto.getEmail());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRealmRoles(Collections.singletonList(role.name()));
        user.setEmailVerified(false);
        user.setRequiredActions(List.of(KeycloakActions.UPDATE_PASSWORD.name()));
        return user;
    }

    private User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            deleteKeycloakUser(user.getKeycloakId());

            String message = String.format("Failed to create user at family with specified id %s. Exc message: %s", user.getFamily(), e.getMessage());
            log.error(message);
            throw new UserServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void deleteKeycloakUser(String kkId) {
        keycloak.realm(realm).users().delete(kkId);
    }

    /*private boolean validateUser(String username, String password) {
        String tokenUrl = KEYCLOAK_URL + "/realms/" + REALM + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", "password");
        map.add("client_id", CLIENT_ID);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException ex) {
            return false;
        }
    }*/
}
