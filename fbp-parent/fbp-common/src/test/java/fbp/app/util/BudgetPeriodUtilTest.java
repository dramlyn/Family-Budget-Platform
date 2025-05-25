package fbp.app.util;

import fbp.app.model.BudgetPeriod;
import fbp.app.repository.BudgetPeriodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Budget Period Util Tests")
class BudgetPeriodUtilTest {

    @Mock
    private BudgetPeriodRepository budgetPeriodRepository;

    @InjectMocks
    private BudgetPeriodUtil budgetPeriodUtil;

    private BudgetPeriod testPeriod;

    @BeforeEach
    void setUp() {
        testPeriod = BudgetPeriod.builder()
                .id(1L)
                .year(2024)
                .month(5)
                .build();
    }

    @Test
    @DisplayName("Should return existing current budget period")
    void shouldReturnExistingCurrentBudgetPeriod() {
        // Given
        when(budgetPeriodRepository.findByYearAndMonth(2025, 5))
                .thenReturn(Optional.of(testPeriod));

        // When
        BudgetPeriod result = budgetPeriodUtil.getOrCreateCurrentBudgetPeriod();

        // Then
        assertThat(result).isEqualTo(testPeriod);
        verify(budgetPeriodRepository).findByYearAndMonth(anyInt(), anyInt());
        verify(budgetPeriodRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create new budget period when none exists")
    void shouldCreateNewBudgetPeriodWhenNoneExists() {
        // Given
        when(budgetPeriodRepository.findByYearAndMonth(anyInt(), anyInt()))
                .thenReturn(Optional.empty());
        when(budgetPeriodRepository.save(any(BudgetPeriod.class)))
                .thenReturn(testPeriod);

        // When
        BudgetPeriod result = budgetPeriodUtil.getOrCreateCurrentBudgetPeriod();

        // Then
        assertThat(result).isEqualTo(testPeriod);
        verify(budgetPeriodRepository).findByYearAndMonth(anyInt(), anyInt());
        verify(budgetPeriodRepository).save(any(BudgetPeriod.class));
    }
}