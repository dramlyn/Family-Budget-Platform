package fbp.app.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Category Model Tests")
class CategoryTest {

    @Test
    @DisplayName("Should create category with builder")
    void shouldCreateCategoryWithBuilder() {
        // When
        Category category = Category.builder()
                .id(1L)
                .name("Groceries")
                .description("Food and household items")
                .build();

        // Then
        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getName()).isEqualTo("Groceries");
        assertThat(category.getDescription()).isEqualTo("Food and household items");
    }

    @Test
    @DisplayName("Should create category with no-args constructor")
    void shouldCreateCategoryWithNoArgsConstructor() {
        // When
        Category category = new Category();

        // Then
        assertThat(category).isNotNull();
        assertThat(category.getId()).isNull();
        assertThat(category.getName()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties")
    void shouldSetAndGetAllProperties() {
        // Given
        Category category = new Category();

        // When
        category.setId(1L);
        category.setName("Entertainment");
        category.setDescription("Movies, games, and fun activities");

        // Then
        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getName()).isEqualTo("Entertainment");
        assertThat(category.getDescription()).isEqualTo("Movies, games, and fun activities");
    }
}