package fbp.app.mapper;

import fbp.app.dto.category.AddCategoryDtoRequest;
import fbp.app.dto.category.CategoryDto;
import fbp.app.model.Category;

import java.time.Instant;

public class CategoryMapper {
    public static Category toCategory(AddCategoryDtoRequest categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .createdAt(Instant.now())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
