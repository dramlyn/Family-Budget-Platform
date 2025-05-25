package fbp.app.rest;

import fbp.app.config.KeycloakActions;
import fbp.app.dto.user.*;
import fbp.app.model.type.Role;
import fbp.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Controller Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserDto testUserDto;
    private RegisterParentDtoRequest registerParentRequest;
    private RegisterUserRequestDto registerUserRequest;
    private ChangePasswordDtoRequest changePasswordRequest;
    private UpdateUserDtoRequest updateUserRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        testUserDto = UserDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .role(Role.PARENT.name())
                .familyId(1L)
                .build();

        registerParentRequest = new RegisterParentDtoRequest();
        registerParentRequest.setFirstName("John");
        registerParentRequest.setLastName("Doe");
        registerParentRequest.setEmail("john.doe@example.com");
        registerParentRequest.setPassword("password123");
        registerParentRequest.setFamilyName("Doe Family");

        registerUserRequest = RegisterUserRequestDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .password("password123")
                .familyId(1L)
                .build();

        changePasswordRequest = new ChangePasswordDtoRequest();
        changePasswordRequest.setEmail("john.doe@example.com");
        changePasswordRequest.setOldPassword("oldpass");
        changePasswordRequest.setNewPassword("newpass");

        updateUserRequest = new UpdateUserDtoRequest();
        updateUserRequest.setFirstName("Updated John");
        updateUserRequest.setLastName("Updated Doe");
    }

    @Test
    @DisplayName("Should register parent successfully")
    void shouldRegisterParentSuccessfully() throws Exception {
        // Given
        when(userService.registerParent(any(RegisterParentDtoRequest.class), eq(Role.PARENT)))
                .thenReturn(testUserDto);

        // When & Then
        mockMvc.perform(post("/v1/users/parent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerParentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).registerParent(any(RegisterParentDtoRequest.class), eq(Role.PARENT));
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() throws Exception {
        // Given
        when(userService.registerUser(any(RegisterUserRequestDto.class), eq(Role.USER)))
                .thenReturn(testUserDto);

        // When & Then
        mockMvc.perform(post("/v1/users/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).registerUser(any(RegisterUserRequestDto.class), eq(Role.USER));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() throws Exception {
        // Given
        doNothing().when(userService).deleteUser("john.doe@example.com");

        // When & Then
        mockMvc.perform(delete("/v1/users/john.doe@example.com"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser("john.doe@example.com");
    }

    @Test
    @DisplayName("Should send forgot password email successfully")
    void shouldSendForgotPasswordEmailSuccessfully() throws Exception {
        // Given
        doNothing().when(userService).sendKeycloakRequiredActionsEmail("john.doe@example.com", KeycloakActions.UPDATE_PASSWORD);

        // When & Then
        mockMvc.perform(post("/v1/users/forgot-password/john.doe@example.com"))
                .andExpect(status().isOk());

        verify(userService).sendKeycloakRequiredActionsEmail("john.doe@example.com", KeycloakActions.UPDATE_PASSWORD);
    }

    @Test
    @DisplayName("Should change password successfully")
    void shouldChangePasswordSuccessfully() throws Exception {
        // Given
        doNothing().when(userService).changePassword(any(ChangePasswordDtoRequest.class));

        // When & Then
        mockMvc.perform(put("/v1/users/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk());

        verify(userService).changePassword(any(ChangePasswordDtoRequest.class));
    }

    @Test
    @DisplayName("Should update user info successfully")
    void shouldUpdateUserInfoSuccessfully() throws Exception {
        // Given
        when(userService.updateUser(any(UpdateUserDtoRequest.class), eq("john.doe@example.com")))
                .thenReturn(testUserDto);

        // When & Then
        mockMvc.perform(put("/v1/users/john.doe@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).updateUser(any(UpdateUserDtoRequest.class), eq("john.doe@example.com"));
    }
}