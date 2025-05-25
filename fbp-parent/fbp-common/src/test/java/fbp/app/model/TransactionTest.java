package fbp.app.model;

import fbp.app.model.type.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Transaction Model Tests")
class TransactionTest {

    @Test
    @DisplayName("Should create transaction with builder")
    void shouldCreateTransactionWithBuilder() {
        // Given
        Instant now = Instant.now();
        User user = User.builder().id(1L).build();
        Category category = Category.builder().id(1L).build();
        BudgetPeriod period = BudgetPeriod.builder().id(1L).build();

        // When
        Transaction transaction = Transaction.builder()
                .id(1L)
                .type(TransactionType.INCOME)
                .amount(100)
                .user(user)
                .category(category)
                .period(period)
                .createdAt(now)
                .build();

        // Then
        assertThat(transaction.getId()).isEqualTo(1L);
        assertThat(transaction.getType()).isEqualTo(TransactionType.INCOME);
        assertThat(transaction.getAmount()).isEqualTo(100);
        assertThat(transaction.getUser()).isEqualTo(user);
        assertThat(transaction.getCategory()).isEqualTo(category);
        assertThat(transaction.getPeriod()).isEqualTo(period);
        assertThat(transaction.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should have default createdAt when using builder")
    void shouldHaveDefaultCreatedAtWhenUsingBuilder() {
        // When
        Transaction transaction = Transaction.builder()
                .type(TransactionType.INCOME)
                .amount(500)
                .build();

        // Then
        assertThat(transaction.getCreatedAt()).isNotNull();
        assertThat(transaction.getCreatedAt()).isBefore(Instant.now().plusSeconds(1));
    }

    @Test
    @DisplayName("Should create transaction with no-args constructor")
    void shouldCreateTransactionWithNoArgsConstructor() {
        // When
        Transaction transaction = new Transaction();

        // Then
        assertThat(transaction).isNotNull();
        assertThat(transaction.getId()).isNull();
        assertThat(transaction.getType()).isNull();
        assertThat(transaction.getAmount()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties")
    void shouldSetAndGetAllProperties() {
        // Given
        Transaction transaction = new Transaction();
        User user = User.builder().id(1L).build();
        Category category = Category.builder().id(1L).build();
        BudgetPeriod period = BudgetPeriod.builder().id(1L).build();
        Instant now = Instant.now();

        // When
        transaction.setId(1L);
        transaction.setType(TransactionType.INCOME);
        transaction.setAmount(250);
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setPeriod(period);
        transaction.setCreatedAt(now);

        // Then
        assertThat(transaction.getId()).isEqualTo(1L);
        assertThat(transaction.getType()).isEqualTo(TransactionType.INCOME);
        assertThat(transaction.getAmount()).isEqualTo(250);
        assertThat(transaction.getUser()).isEqualTo(user);
        assertThat(transaction.getCategory()).isEqualTo(category);
        assertThat(transaction.getPeriod()).isEqualTo(period);
        assertThat(transaction.getCreatedAt()).isEqualTo(now);
    }
}