package fbp.app.service;

import fbp.app.config.KeycloakActions;
import fbp.app.dto.family.CreateFamilyDtoRequest;
import fbp.app.dto.user.*;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.UserMapper;
import fbp.app.model.Family;
import fbp.app.model.User;
import fbp.app.model.type.Role;
import fbp.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Keycloak keycloak;

    @Mock
    private FamilyService familyService;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @Mock
    private UserResource userResource;

    @Mock
    private Response response;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Family testFamily;
    private RegisterUserRequestDto registerRequest;
    private RegisterParentDtoRequest registerParentRequest;
    private AddParentToFamilyDtoRequest addParentRequest;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "realm", "test-realm");

        testFamily = Family.builder()
                .id(1L)
                .name("Test Family")
                .description("Test Family Description")
                .build();

        testUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .keycloakId("keycloak-id-123")
                .role(Role.USER)
                .family(testFamily)
                .createdAt(Instant.now())
                .build();

        registerRequest = RegisterUserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .familyId(1L)
                .build();

        registerParentRequest = RegisterParentDtoRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .familyName("Doe Family")
                .build();

        addParentRequest = new AddParentToFamilyDtoRequest();
        addParentRequest.setFirstName("Bob");
        addParentRequest.setLastName("Smith");
        addParentRequest.setEmail("bob.smith@example.com");

        // Setup Keycloak mocks in each test method where needed
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        // Given
        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(anyString())).thenReturn(userResource);
        when(familyService.findFamilyById(1L)).thenReturn(testFamily);
        when(usersResource.search(anyString())).thenReturn(List.of());
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(201);
        when(response.getLocation()).thenReturn(URI.create("/users/keycloak-id-123"));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        try (MockedStatic<UserMapper> userMapperMock = mockStatic(UserMapper.class)) {
            KeycloakUserDto keycloakUserDto = KeycloakUserDto.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@example.com")
                    .build();

            UserDto expectedDto = UserDto.builder()
                    .id(1L)
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@example.com")
                    .build();

            userMapperMock.when(() -> UserMapper.toKeycloakUserDto(any(RegisterUserRequestDto.class)))
                    .thenReturn(keycloakUserDto);
            userMapperMock.when(() -> UserMapper.toUserDto(any(User.class)))
                    .thenReturn(expectedDto);

            // When
            UserDto result = userService.registerUser(registerRequest, Role.USER);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getEmail()).isEqualTo("john.doe@example.com");

            verify(familyService).findFamilyById(1L);
            verify(userRepository).save(any(User.class));
            verify(usersResource).create(any(UserRepresentation.class));
        }
    }

    @Test
    @DisplayName("Should register parent successfully")
    void shouldRegisterParentSuccessfully() {
        // Given
        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(anyString())).thenReturn(userResource);
        when(usersResource.search(anyString())).thenReturn(List.of());
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(201);
        when(response.getLocation()).thenReturn(URI.create("/users/keycloak-id-456"));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        CreateFamilyDtoRequest familyRequest = new CreateFamilyDtoRequest();
        familyRequest.setName("Doe Family");
        familyRequest.setDescription("Doe Family");
        when(familyService.createFamily(any(CreateFamilyDtoRequest.class))).thenReturn(testFamily);

        try (MockedStatic<UserMapper> userMapperMock = mockStatic(UserMapper.class)) {
            KeycloakUserDto keycloakUserDto = KeycloakUserDto.builder()
                    .firstName("Jane")
                    .lastName("Doe")
                    .email("jane.doe@example.com")
                    .build();

            UserDto expectedDto = UserDto.builder()
                    .id(1L)
                    .firstName("Jane")
                    .lastName("Doe")
                    .email("jane.doe@example.com")
                    .build();

            userMapperMock.when(() -> UserMapper.toKeycloakUserDto(any(RegisterParentDtoRequest.class)))
                    .thenReturn(keycloakUserDto);
            userMapperMock.when(() -> UserMapper.toUserDto(any(User.class)))
                    .thenReturn(expectedDto);

            // When
            UserDto result = userService.registerParent(registerParentRequest, Role.PARENT);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("Jane");

            verify(familyService).createFamily(any(CreateFamilyDtoRequest.class));
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    @DisplayName("Should add parent to family successfully")
    void shouldAddParentToFamilySuccessfully() {
        // Given
        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(anyString())).thenReturn(userResource);
        when(familyService.findFamilyById(1L)).thenReturn(testFamily);
        when(userRepository.findByFamilyId(1L)).thenReturn(List.of(testUser));
        when(usersResource.search(anyString())).thenReturn(List.of());
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(201);
        when(response.getLocation()).thenReturn(URI.create("/users/keycloak-id-789"));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        try (MockedStatic<UserMapper> userMapperMock = mockStatic(UserMapper.class)) {
            KeycloakUserDto keycloakUserDto = KeycloakUserDto.builder()
                    .firstName("Bob")
                    .lastName("Smith")
                    .email("bob.smith@example.com")
                    .build();

            UserDto expectedDto = UserDto.builder()
                    .id(1L)
                    .firstName("Bob")
                    .lastName("Smith")
                    .email("bob.smith@example.com")
                    .build();

            userMapperMock.when(() -> UserMapper.toKeycloakUserDto(any(AddParentToFamilyDtoRequest.class)))
                    .thenReturn(keycloakUserDto);
            userMapperMock.when(() -> UserMapper.toUserDto(any(User.class)))
                    .thenReturn(expectedDto);

            // When
            UserDto result = userService.addParentToFamily(addParentRequest, 1L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFirstName()).isEqualTo("Bob");

            verify(familyService).findFamilyById(1L);
            verify(userRepository).findByFamilyId(1L);
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when adding third parent to family")
    void shouldThrowExceptionWhenAddingThirdParent() {
        // Given
        User parent1 = User.builder().id(1L).role(Role.PARENT).build();
        User parent2 = User.builder().id(2L).role(Role.PARENT).build();
        when(familyService.findFamilyById(1L)).thenReturn(testFamily);
        when(userRepository.findByFamilyId(1L)).thenReturn(List.of(parent1, parent2));

        // When & Then
        assertThatThrownBy(() -> userService.addParentToFamily(addParentRequest, 1L))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("already 2 parents");

        verify(familyService).findFamilyById(1L);
        verify(userRepository).findByFamilyId(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.deleteUser("john.doe@example.com");

        // Then
        verify(userRepository).findByEmail("john.doe@example.com");
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should send keycloak required actions email successfully")
    void shouldSendKeycloakRequiredActionsEmailSuccessfully() {
        // Given
        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(anyString())).thenReturn(userResource);
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId("keycloak-id-123");
        when(usersResource.search("john.doe@example.com")).thenReturn(List.of(userRep));
        doNothing().when(userResource).executeActionsEmail(anyList());

        // When
        userService.sendKeycloakRequiredActionsEmail("john.doe@example.com", KeycloakActions.UPDATE_PASSWORD);

        // Then
        verify(usersResource).search("john.doe@example.com");
        verify(userResource).executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }

    @Test
    @DisplayName("Should throw exception when user not found in keycloak")
    void shouldThrowExceptionWhenUserNotFoundInKeycloak() {
        // Given
        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.search("nonexistent@example.com")).thenReturn(List.of());

        // When & Then
        assertThatThrownBy(() -> userService.sendKeycloakRequiredActionsEmail("nonexistent@example.com", KeycloakActions.UPDATE_PASSWORD))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("not found");

        verify(usersResource).search("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Given
        UpdateUserDtoRequest updateRequest = new UpdateUserDtoRequest();
        updateRequest.setFirstName("Updated John");
        updateRequest.setLastName("Updated Doe");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        try (MockedStatic<UserMapper> userMapperMock = mockStatic(UserMapper.class)) {
            UserDto expectedDto = UserDto.builder()
                    .id(1L)
                    .firstName("Updated John")
                    .lastName("Updated Doe")
                    .email("john.doe@example.com")
                    .build();

            userMapperMock.when(() -> UserMapper.toUserDto(any(User.class)))
                    .thenReturn(expectedDto);

            // When
            UserDto result = userService.updateUser(updateRequest, "john.doe@example.com");

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFirstName()).isEqualTo("Updated John");
            assertThat(result.getLastName()).isEqualTo("Updated Doe");

            verify(userRepository).findByEmail("john.doe@example.com");
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Given
        UpdateUserDtoRequest updateRequest = new UpdateUserDtoRequest();
        updateRequest.setFirstName("Updated John");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(updateRequest, "nonexistent@example.com"))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("not found");

        verify(userRepository).findByEmail("nonexistent@example.com");
        verify(userRepository, never()).save(any());
    }
}