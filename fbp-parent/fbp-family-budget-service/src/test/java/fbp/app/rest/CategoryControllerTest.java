package fbp.app.rest;

import fbp.app.dto.category.AddCategoryDtoRequest;
import fbp.app.dto.category.CategoryDto;
import fbp.app.dto.category.CategoryExpenseReportDto;
import fbp.app.exception.UserServiceException;
import fbp.app.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import fbp.app.util.FamilyBudgetPlatformUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category Controller Tests")
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private FamilyBudgetPlatformUtils familyBudgetPlatformUtils;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private CategoryDto testCategoryDto;
    private AddCategoryDtoRequest addCategoryRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
        
        testCategoryDto = CategoryDto.builder()
                .id(1L)
                .name("Groceries")
                .description("Food and grocery expenses")
                .build();

        addCategoryRequest = AddCategoryDtoRequest.builder()
                .name("Groceries")
                .description("Food and grocery expenses")
                .build();
    }

    @Test
    @DisplayName("Should add category successfully")
    void shouldAddCategorySuccessfully() throws Exception {
        // Given
        when(categoryService.addCategory(any(AddCategoryDtoRequest.class))).thenReturn(testCategoryDto);

        // When & Then
        mockMvc.perform(post("/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Groceries"))
                .andExpect(jsonPath("$.description").value("Food and grocery expenses"));

        verify(categoryService).addCategory(any(AddCategoryDtoRequest.class));
    }

    @Test
    @DisplayName("Should get category by ID successfully")
    void shouldGetCategoryByIdSuccessfully() throws Exception {
        // Given
        when(categoryService.getCategory(1L)).thenReturn(testCategoryDto);

        // When & Then
        mockMvc.perform(get("/v1/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Groceries"))
                .andExpect(jsonPath("$.description").value("Food and grocery expenses"));

        verify(categoryService).getCategory(1L);
    }

    @Test
    @DisplayName("Should get all categories successfully")
    void shouldGetAllCategoriesSuccessfully() throws Exception {
        // Given
        List<CategoryDto> categories = Arrays.asList(testCategoryDto);
        when(categoryService.getAllCategories()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/v1/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Groceries"));

        verify(categoryService).getAllCategories();
    }

    @Test
    @DisplayName("Should delete category successfully")
    void shouldDeleteCategorySuccessfully() throws Exception {
        // Given
        doNothing().when(categoryService).deleteCategory(1L);

        // When & Then
        mockMvc.perform(delete("/v1/category/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(1L);
    }

    @Test
    @DisplayName("Should get user expense report successfully")
    void shouldGetUserExpenseReportSuccessfully() throws Exception {
        // Given
        CategoryExpenseReportDto reportDto = CategoryExpenseReportDto.builder()
                .userId(1L)
                .categoryId(1L)
                .expense(500)
                .build();
        when(familyBudgetPlatformUtils.getCurrentUserKkId()).thenReturn("test-user-123");
        when(categoryService.getUserExpenseReport(1L, 1L, "test-user-123")).thenReturn(reportDto);

        // When & Then
        mockMvc.perform(get("/v1/category/expense-report")
                        .param("periodId", "1")
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.expense").value(500));

        verify(categoryService).getUserExpenseReport(1L, 1L, "test-user-123");
    }

    @Test
    @DisplayName("Should handle invalid request data")
    void shouldHandleInvalidRequestData() throws Exception {
        // Given - Empty category name
        AddCategoryDtoRequest invalidRequest = AddCategoryDtoRequest.builder()
                .name("")
                .description("Food and grocery expenses")
                .build();

        // When & Then
        mockMvc.perform(post("/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}