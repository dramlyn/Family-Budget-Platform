package fbp.app.rest;

import fbp.app.dto.category.AddCategoryDtoRequest;
import fbp.app.dto.category.CategoryDto;
import fbp.app.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid AddCategoryDtoRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.addCategory(categoryRequest));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategory(categoryId));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
