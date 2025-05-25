package fbp.app.rest;

import fbp.app.dto.family.CreateFamilyDtoRequest;
import fbp.app.dto.family.FamilyDto;
import fbp.app.dto.user.AddParentToFamilyDtoRequest;
import fbp.app.dto.user.UserDto;
import fbp.app.model.type.Role;
import fbp.app.service.FamilyService;
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
@DisplayName("Family Controller Tests")
class FamilyControllerTest {

    @Mock
    private FamilyService familyService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FamilyController familyController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private FamilyDto testFamilyDto;
    private UserDto testUserDto;
    private CreateFamilyDtoRequest createFamilyRequest;
    private AddParentToFamilyDtoRequest addParentRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(familyController).build();
        objectMapper = new ObjectMapper();

        testFamilyDto = FamilyDto.builder()
                .id(1L)
                .name("Test Family")
                .description("Test Family Description")
                .build();

        testUserDto = UserDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .role(Role.PARENT.name())
                .familyId(1L)
                .build();

        createFamilyRequest = new CreateFamilyDtoRequest();
        createFamilyRequest.setName("Test Family");
        createFamilyRequest.setDescription("Test Family Description");

        addParentRequest = new AddParentToFamilyDtoRequest();
        addParentRequest.setFirstName("John");
        addParentRequest.setLastName("Doe");
        addParentRequest.setEmail("john.doe@example.com");
        addParentRequest.setPassword("password123");
    }

    @Test
    @DisplayName("Should create family successfully")
    void shouldCreateFamilySuccessfully() throws Exception {
        // Given
        when(familyService.createAndReturnFamilyDto(any(CreateFamilyDtoRequest.class)))
                .thenReturn(testFamilyDto);

        // When & Then
        mockMvc.perform(post("/v1/family")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFamilyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Family"))
                .andExpect(jsonPath("$.description").value("Test Family Description"));

        verify(familyService).createAndReturnFamilyDto(any(CreateFamilyDtoRequest.class));
    }

    @Test
    @DisplayName("Should add parent to family successfully")
    void shouldAddParentToFamilySuccessfully() throws Exception {
        // Given
        when(userService.addParentToFamily(any(AddParentToFamilyDtoRequest.class), eq(1L)))
                .thenReturn(testUserDto);

        // When & Then
        mockMvc.perform(post("/v1/family/add-parent/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addParentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.familyId").value(1L));

        verify(userService).addParentToFamily(any(AddParentToFamilyDtoRequest.class), eq(1L));
    }
}