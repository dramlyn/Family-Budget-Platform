package fbp.app.rest;

import fbp.app.dto.category.AddCategoryDtoRequest;
import fbp.app.dto.category.CategoryDto;
import fbp.app.dto.category.CategoryExpenseReportDto;
import fbp.app.service.CategoryService;
import fbp.app.util.FamilyBudgetPlatformUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final FamilyBudgetPlatformUtils utils;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PARENT')")
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

    @PreAuthorize("hasRole('ROLE_PARENT')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/expense-report")
    public ResponseEntity<CategoryExpenseReportDto> getUserExpenseReport(
            @RequestParam(value = "periodId") Long periodId,
            @RequestParam(value = "categoryId") Long categoryId)
    {
        return ResponseEntity.ok(categoryService.getUserExpenseReport(periodId, categoryId, utils.getCurrentUserKkId()));
    }
}
