package fbp.app.mapper;

import fbp.app.dto.user.*;
import fbp.app.model.Family;
import fbp.app.model.User;
import fbp.app.model.type.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DisplayName("User Mapper Tests")
class UserMapperTest {

    private User testUser;
    private Family testFamily;
    private RegisterUserRequestDto registerUserRequest;
    private RegisterParentDtoRequest registerParentRequest;
    private AddParentToFamilyDtoRequest addParentRequest;

    @BeforeEach
    void setUp() {
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
                .role(Role.PARENT)
                .family(testFamily)
                .createdAt(Instant.now())
                .build();

        registerUserRequest = RegisterUserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .familyId(1L)
                .build();

        registerParentRequest = RegisterParentDtoRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .familyName("Smith Family")
                .build();

        addParentRequest = new AddParentToFamilyDtoRequest();
        addParentRequest.setFirstName("Bob");
        addParentRequest.setLastName("Johnson");
        addParentRequest.setEmail("bob.johnson@example.com");
    }

    @Test
    @DisplayName("Should map User to UserDto successfully")
    void shouldMapUserToUserDtoSuccessfully() {
        // When
        UserDto result = UserMapper.toUserDto(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getRole()).isEqualTo(Role.PARENT.name());
        assertThat(result.getFamilyId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should map RegisterUserRequestDto to KeycloakUserDto successfully")
    void shouldMapRegisterUserRequestDtoToKeycloakUserDtoSuccessfully() {
        // When
        KeycloakUserDto result = UserMapper.toKeycloakUserDto(registerUserRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should map RegisterParentDtoRequest to KeycloakUserDto successfully")
    void shouldMapRegisterParentDtoRequestToKeycloakUserDtoSuccessfully() {
        // When
        KeycloakUserDto result = UserMapper.toKeycloakUserDto(registerParentRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    @DisplayName("Should map AddParentToFamilyDtoRequest to KeycloakUserDto successfully")
    void shouldMapAddParentToFamilyDtoRequestToKeycloakUserDtoSuccessfully() {
        // When
        KeycloakUserDto result = UserMapper.toKeycloakUserDto(addParentRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Bob");
        assertThat(result.getLastName()).isEqualTo("Johnson");
        assertThat(result.getEmail()).isEqualTo("bob.johnson@example.com");
    }
}