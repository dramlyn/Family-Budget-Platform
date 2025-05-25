package fbp.app.mapper;

import fbp.app.dto.family.CreateFamilyDtoRequest;
import fbp.app.dto.family.FamilyDto;
import fbp.app.model.Family;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Family Mapper Tests")
class FamilyMapperTest {

    private Family testFamily;
    private CreateFamilyDtoRequest createRequest;

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
    }

    @Test
    @DisplayName("Should map Family to FamilyDto successfully")
    void shouldMapFamilyToFamilyDtoSuccessfully() {
        // When
        FamilyDto result = FamilyMapper.toFamilyDto(testFamily);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Family");
        assertThat(result.getDescription()).isEqualTo("Test Family Description");
    }

    @Test
    @DisplayName("Should map CreateFamilyDtoRequest to Family successfully")
    void shouldMapCreateFamilyDtoRequestToFamilySuccessfully() {
        // When
        Family result = FamilyMapper.toFamily(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Family");
        assertThat(result.getDescription()).isEqualTo("Test Family Description");
        assertThat(result.getId()).isNull(); // ID should be null for new entities
    }
}