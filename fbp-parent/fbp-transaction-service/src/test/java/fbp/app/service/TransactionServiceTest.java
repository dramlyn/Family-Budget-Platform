package fbp.app.service;

import fbp.app.dto.CreateTransactionDtoRequest;
import fbp.app.dto.TransactionDto;
import fbp.app.exception.UserServiceException;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.Category;
import fbp.app.model.Transaction;
import fbp.app.model.User;
import fbp.app.model.type.TransactionType;
import fbp.app.repository.CategoryRepository;
import fbp.app.repository.TransactionRepository;
import fbp.app.repository.UserRepository;
import fbp.app.util.BudgetPeriodUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Transaction Service Tests")
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BudgetPeriodUtil budgetPeriodUtil;

    @InjectMocks
    private TransactionService transactionService;

    private User testUser;
    private Category testCategory;
    private BudgetPeriod testPeriod;
    private Transaction testTransaction;
    private CreateTransactionDtoRequest createRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .build();

        testCategory = Category.builder()
                .id(1L)
                .build();

        testPeriod = BudgetPeriod.builder()
                .id(1L)
                .build();

        testTransaction = Transaction.builder()
                .id(1L)
                .amount(100)
                .type(TransactionType.INCOME)
                .user(testUser)
                .category(testCategory)
                .period(testPeriod)
                .createdAt(Instant.now())
                .build();

        createRequest = new CreateTransactionDtoRequest();
        createRequest.setUserId(1L);
        createRequest.setCategoryId(1L);
        createRequest.setAmount(100);
        createRequest.setType(TransactionType.INCOME);
    }

    @Test
    @DisplayName("Should create transaction successfully")
    void shouldCreateTransactionSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(budgetPeriodUtil.getOrCreateCurrentBudgetPeriod()).thenReturn(testPeriod);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        // When
        TransactionDto result = transactionService.createTransaction(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualTo(100);
        assertThat(result.getType()).isEqualTo(TransactionType.INCOME);

        verify(userRepository).findById(1L);
        verify(categoryRepository).findById(1L);
        verify(budgetPeriodUtil).getOrCreateCurrentBudgetPeriod();
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> transactionService.createTransaction(createRequest))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("User with id 1 not found");

        verify(userRepository).findById(1L);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get transaction by ID successfully")
    void shouldGetTransactionByIdSuccessfully() {
        // Given
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));

        // When
        TransactionDto result = transactionService.getTransactionById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualTo(100);

        verify(transactionRepository).findById(1L);
    }

    @Test
    @DisplayName("Should get all transactions by parameters")
    void shouldGetAllTransactionsByParameters() {
        // Given
        List<Transaction> transactions = Arrays.asList(testTransaction);
        when(transactionRepository.findTransactionsWithParams(1L, 1L)).thenReturn(transactions);

        // When
        List<TransactionDto> result = transactionService.getAllByParam(1L, 1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);

        verify(transactionRepository).findTransactionsWithParams(1L, 1L);
    }

    @Test
    @DisplayName("Should delete transaction successfully")
    void shouldDeleteTransactionSuccessfully() {
        // Given
        doNothing().when(transactionRepository).deleteById(1L);

        // When
        transactionService.deleteTransactionById(1L);

        // Then
        verify(transactionRepository).deleteById(1L);
    }
}