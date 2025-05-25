package fbp.app.service;

import fbp.app.dto.category.AddCategoryDtoRequest;
import fbp.app.dto.category.CategoryDto;
import fbp.app.dto.category.CategoryExpenseReportDto;
import fbp.app.exception.UserServiceException;
import fbp.app.model.Category;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.User;
import fbp.app.model.Transaction;
import fbp.app.repository.CategoryRepository;
import fbp.app.repository.TransactionRepository;
import fbp.app.repository.UserRepository;
import fbp.app.util.BudgetPeriodUtil;
import fbp.app.util.ModelFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category Service Tests")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BudgetPeriodUtil budgetPeriodUtil;

    @Mock
    private ModelFinder modelFinder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;
    private AddCategoryDtoRequest addCategoryRequest;
    private User testUser;
    private BudgetPeriod testPeriod;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("Groceries")
                .description("Food and grocery expenses")
                .build();

        addCategoryRequest = AddCategoryDtoRequest.builder()
                .name("Groceries")
                .description("Food and grocery expenses")
                .build();

        testUser = User.builder()
                .id(1L)
                .keycloakId("test-user-123")
                .build();

        testPeriod = BudgetPeriod.builder()
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("Should add category successfully")
    void shouldAddCategorySuccessfully() {
        // Given
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // When
        CategoryDto result = categoryService.addCategory(addCategoryRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Groceries");
        assertThat(result.getDescription()).isEqualTo("Food and grocery expenses");
        
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Should get category by ID successfully")
    void shouldGetCategoryByIdSuccessfully() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // When
        CategoryDto result = categoryService.getCategory(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Groceries");
        
        verify(categoryRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    void shouldThrowExceptionWhenCategoryNotFound() {
        // Given
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.getCategory(999L))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Category with id 999 not found");
        
        verify(categoryRepository).findById(999L);
    }

    @Test
    @DisplayName("Should get all categories successfully")
    void shouldGetAllCategoriesSuccessfully() {
        // Given
        List<Category> categories = Arrays.asList(testCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        // When
        List<CategoryDto> result = categoryService.getAllCategories();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Groceries");
        
        verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName("Should delete category successfully")
    void shouldDeleteCategorySuccessfully() {
        // Given
        doNothing().when(categoryRepository).deleteById(1L);

        // When
        categoryService.deleteCategory(1L);

        // Then
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle delete category exception")
    void shouldHandleDeleteCategoryException() {
        // Given
        doThrow(new RuntimeException("Database error")).when(categoryRepository).deleteById(1L);

        // When & Then
        assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Error when delete category with id 1");
        
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should generate user expense report successfully")
    void shouldGenerateUserExpenseReportSuccessfully() {
        // Given
        Long periodId = 1L;
        Long categoryId = 1L;
        String keycloakId = "test-user-123";
        
        Transaction transaction1 = Transaction.builder().amount(100).build();
        Transaction transaction2 = Transaction.builder().amount(200).build();
        List<Transaction> expenses = Arrays.asList(transaction1, transaction2);

        when(modelFinder.findBudgetPeriod(periodId)).thenReturn(testPeriod);
        when(modelFinder.findCategory(categoryId)).thenReturn(testCategory);
        when(modelFinder.findUserByKeycloakId(keycloakId)).thenReturn(testUser);
        when(transactionRepository.findAllUserExpensesByCategoryAndPeriod(periodId, categoryId, testUser.getId()))
                .thenReturn(expenses);

        // When
        CategoryExpenseReportDto result = categoryService.getUserExpenseReport(periodId, categoryId, keycloakId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getExpense()).isEqualTo(300); // 100 + 200
        assertThat(result.getUserId()).isEqualTo(testUser.getId());
        assertThat(result.getCategoryId()).isEqualTo(categoryId);
        
        verify(modelFinder).findBudgetPeriod(periodId);
        verify(modelFinder).findCategory(categoryId);
        verify(modelFinder).findUserByKeycloakId(keycloakId);
        verify(transactionRepository).findAllUserExpensesByCategoryAndPeriod(periodId, categoryId, testUser.getId());
    }
}