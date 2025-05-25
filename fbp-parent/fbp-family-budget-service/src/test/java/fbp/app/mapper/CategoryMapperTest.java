package fbp.app.mapper;

import fbp.app.dto.category.AddCategoryDtoRequest;
import fbp.app.dto.category.CategoryDto;
import fbp.app.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Category Mapper Tests")
class CategoryMapperTest {

    @Test
    @DisplayName("Should convert AddCategoryDtoRequest to Category")
    void shouldConvertAddCategoryDtoRequestToCategory() {
        // Given
        AddCategoryDtoRequest request = AddCategoryDtoRequest.builder()
                .name("Groceries")
                .description("Food and grocery expenses")
                .build();

        // When
        Category result = CategoryMapper.toCategory(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Groceries");
        assertThat(result.getDescription()).isEqualTo("Food and grocery expenses");
    }

    @Test
    @DisplayName("Should convert Category to CategoryDto")
    void shouldConvertCategoryToCategoryDto() {
        // Given
        Category category = Category.builder()
                .id(1L)
                .name("Transportation")
                .description("Car and public transport expenses")
                .build();

        // When
        CategoryDto result = CategoryMapper.toCategoryDto(category);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Transportation");
        assertThat(result.getDescription()).isEqualTo("Car and public transport expenses");
    }

    @Test
    @DisplayName("Should handle null values gracefully in toCategory")
    void shouldHandleNullValuesInToCategory() {
        // When & Then
        assertThatThrownBy(() -> CategoryMapper.toCategory(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should handle null values gracefully in toCategoryDto")
    void shouldHandleNullValuesInToCategoryDto() {
        // When & Then
        assertThatThrownBy(() -> CategoryMapper.toCategoryDto(null))
                .isInstanceOf(NullPointerException.class);
    }
}