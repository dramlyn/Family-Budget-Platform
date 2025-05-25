package fbp.app.service;

import fbp.app.dto.family.CreateFamilyDtoRequest;
import fbp.app.dto.family.FamilyDto;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.FamilyMapper;
import fbp.app.model.Family;
import fbp.app.repository.FamilyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Family Service Tests")
class FamilyServiceTest {

    @Mock
    private FamilyRepository familyRepository;

    @InjectMocks
    private FamilyService familyService;

    private Family testFamily;
    private CreateFamilyDtoRequest createRequest;
    private FamilyDto testFamilyDto;

    @BeforeEach
    void setUp() {
        testFamily = Family.builder()
                .id(1L)
                .name("Test Family")
                .description("Test Family Description")
                .build();

        createRequest = new CreateFamilyDtoRequest();
        createRequest.setName("Test Family");
        createRequest.setDescription("Test Family Description");

        testFamilyDto = FamilyDto.builder()
                .id(1L)
                .name("Test Family")
                .description("Test Family Description")
                .build();
    }

    @Test
    @DisplayName("Should create and return family DTO successfully")
    void shouldCreateAndReturnFamilyDtoSuccessfully() {
        // Given
        when(familyRepository.save(any(Family.class))).thenReturn(testFamily);

        try (MockedStatic<FamilyMapper> familyMapperMock = mockStatic(FamilyMapper.class)) {
            familyMapperMock.when(() -> FamilyMapper.toFamily(any(CreateFamilyDtoRequest.class)))
                    .thenReturn(testFamily);
            familyMapperMock.when(() -> FamilyMapper.toFamilyDto(any(Family.class)))
                    .thenReturn(testFamilyDto);

            // When
            FamilyDto result = familyService.createAndReturnFamilyDto(createRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Test Family");
            assertThat(result.getDescription()).isEqualTo("Test Family Description");

            verify(familyRepository).save(any(Family.class));
        }
    }

    @Test
    @DisplayName("Should create family successfully")
    void shouldCreateFamilySuccessfully() {
        // Given
        when(familyRepository.save(any(Family.class))).thenReturn(testFamily);

        try (MockedStatic<FamilyMapper> familyMapperMock = mockStatic(FamilyMapper.class)) {
            familyMapperMock.when(() -> FamilyMapper.toFamily(any(CreateFamilyDtoRequest.class)))
                    .thenReturn(testFamily);

            // When
            Family result = familyService.createFamily(createRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Test Family");
            assertThat(result.getDescription()).isEqualTo("Test Family Description");

            verify(familyRepository).save(any(Family.class));
        }
    }

    @Test
    @DisplayName("Should find family by ID successfully")
    void shouldFindFamilyByIdSuccessfully() {
        // Given
        when(familyRepository.findById(1L)).thenReturn(Optional.of(testFamily));

        // When
        Family result = familyService.findFamilyById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Family");

        verify(familyRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when family not found")
    void shouldThrowExceptionWhenFamilyNotFound() {
        // Given
        when(familyRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> familyService.findFamilyById(999L))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Family with specified id 999 not found");

        verify(familyRepository).findById(999L);
    }
}