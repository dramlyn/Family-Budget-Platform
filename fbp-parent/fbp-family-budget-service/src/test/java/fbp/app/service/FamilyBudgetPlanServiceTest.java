package fbp.app.service;

import fbp.app.dto.family_budget_plan.CreateFamilyBudgetPlanDto;
import fbp.app.dto.family_budget_plan.CreateUserCategoryPlanDtoRequest;
import fbp.app.dto.family_budget_plan.FamilyBudgetPlanDto;
import fbp.app.exception.UserServiceException;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.Family;
import fbp.app.model.FamilyBudgetPlan;
import fbp.app.repository.BudgetPeriodRepository;
import fbp.app.repository.FamilyBudgetPlanRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Family Budget Plan Service Tests")
class FamilyBudgetPlanServiceTest {

    @Mock
    private FamilyBudgetPlanRepository familyBudgetPlanRepository;

    @Mock
    private UserCategoryPlanService userCategoryPlanService;

    @Mock
    private ModelFinder modelFinder;

    @Mock
    private BudgetPeriodUtil budgetPeriodUtil;

    @Mock
    private BudgetPeriodRepository budgetPeriodRepository;

    @InjectMocks
    private FamilyBudgetPlanService familyBudgetPlanService;

    private Family testFamily;
    private BudgetPeriod testPeriod;
    private FamilyBudgetPlan testFamilyBudgetPlan;
    private CreateFamilyBudgetPlanDto createRequest;

    @BeforeEach
    void setUp() {
        testFamily = Family.builder()
                .id(1L)
                .name("Test Family")
                .build();

        testPeriod = BudgetPeriod.builder()
                .id(1L)
                .build();

        testFamilyBudgetPlan = FamilyBudgetPlan.builder()
                .id(1L)
                .family(testFamily)
                .period(testPeriod)
                .limit(5000)
                .build();

        CreateUserCategoryPlanDtoRequest userPlan1 = CreateUserCategoryPlanDtoRequest.builder()
                .limit(2000)
                .build();
        
        CreateUserCategoryPlanDtoRequest userPlan2 = CreateUserCategoryPlanDtoRequest.builder()
                .limit(1500)
                .build();

        createRequest = CreateFamilyBudgetPlanDto.builder()
                .familyId(1L)
                .budgetLimit(5000)
                .familyMembersPlan(Arrays.asList(userPlan1, userPlan2))
                .build();
    }

    @Test
    @DisplayName("Should create monthly family budget plan successfully")
    void shouldCreateMonthlyFamilyBudgetPlanSuccessfully() {
        // Given
        when(modelFinder.findFamily(1L)).thenReturn(testFamily);
        when(budgetPeriodUtil.getOrCreateCurrentBudgetPeriod()).thenReturn(testPeriod);
        when(familyBudgetPlanRepository.save(any(FamilyBudgetPlan.class))).thenReturn(testFamilyBudgetPlan);
        // Mock createUserCategoryPlan method if it returns something, or remove if it's void

        // When
        FamilyBudgetPlanDto result = familyBudgetPlanService.createMonthlyFamilyBudgetPlan(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFamilyId()).isEqualTo(1L);
        assertThat(result.getFamilyBudgetLimit()).isEqualTo(5000);
        assertThat(result.getPeriodId()).isEqualTo(1L);

        verify(modelFinder).findFamily(1L);
        verify(budgetPeriodUtil).getOrCreateCurrentBudgetPeriod();
        verify(familyBudgetPlanRepository).save(any(FamilyBudgetPlan.class));
        verify(userCategoryPlanService, times(2)).createUserCategoryPlan(any());
    }

    @Test
    @DisplayName("Should throw exception when family members limit exceeds family budget")
    void shouldThrowExceptionWhenFamilyMembersLimitExceedsFamilyBudget() {
        // Given - Family budget limit is 5000, but members plan total is 6000
        CreateUserCategoryPlanDtoRequest userPlan1 = CreateUserCategoryPlanDtoRequest.builder()
                .limit(3000)
                .build();
        
        CreateUserCategoryPlanDtoRequest userPlan2 = CreateUserCategoryPlanDtoRequest.builder()
                .limit(3000)
                .build();

        CreateFamilyBudgetPlanDto invalidRequest = CreateFamilyBudgetPlanDto.builder()
                .familyId(1L)
                .budgetLimit(5000)
                .familyMembersPlan(Arrays.asList(userPlan1, userPlan2))
                .build();

        when(modelFinder.findFamily(1L)).thenReturn(testFamily);
        when(budgetPeriodUtil.getOrCreateCurrentBudgetPeriod()).thenReturn(testPeriod);

        // When & Then
        assertThatThrownBy(() -> familyBudgetPlanService.createMonthlyFamilyBudgetPlan(invalidRequest))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Family members limit sum more than family budget limit");

        verify(modelFinder).findFamily(1L);
        verify(budgetPeriodUtil).getOrCreateCurrentBudgetPeriod();
        verify(familyBudgetPlanRepository, never()).save(any());
        verify(userCategoryPlanService, never()).createUserCategoryPlan(any());
    }

    @Test
    @DisplayName("Should get family budget plan by family ID with current period")
    void shouldGetFamilyBudgetPlanByFamilyIdWithCurrentPeriod() {
        // Given
        when(budgetPeriodUtil.getOrCreateCurrentBudgetPeriod()).thenReturn(testPeriod);
        when(familyBudgetPlanRepository.findByFamilyIdAndPeriodId(1L, 1L))
                .thenReturn(Optional.of(testFamilyBudgetPlan));

        // When
        FamilyBudgetPlanDto result = familyBudgetPlanService.getFamilyBudgetPlanByFamilyId(1L, null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFamilyId()).isEqualTo(1L);
        assertThat(result.getPeriodId()).isEqualTo(1L);

        verify(budgetPeriodUtil).getOrCreateCurrentBudgetPeriod();
        verify(familyBudgetPlanRepository).findByFamilyIdAndPeriodId(1L, 1L);
    }

    @Test
    @DisplayName("Should get family budget plan by family ID with specific period")
    void shouldGetFamilyBudgetPlanByFamilyIdWithSpecificPeriod() {
        // Given
        Long specificPeriodId = 2L;
        BudgetPeriod specificPeriod = BudgetPeriod.builder().id(specificPeriodId).build();
        
        when(budgetPeriodRepository.findById(specificPeriodId)).thenReturn(Optional.of(specificPeriod));
        when(familyBudgetPlanRepository.findByFamilyIdAndPeriodId(1L, specificPeriodId))
                .thenReturn(Optional.of(testFamilyBudgetPlan));

        // When
        FamilyBudgetPlanDto result = familyBudgetPlanService.getFamilyBudgetPlanByFamilyId(1L, specificPeriodId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFamilyId()).isEqualTo(1L);

        verify(budgetPeriodRepository).findById(specificPeriodId);
        verify(familyBudgetPlanRepository).findByFamilyIdAndPeriodId(1L, specificPeriodId);
        verify(budgetPeriodUtil, never()).getOrCreateCurrentBudgetPeriod();
    }

    @Test
    @DisplayName("Should handle family not found exception")
    void shouldHandleFamilyNotFoundException() {
        // Given
        when(modelFinder.findFamily(999L))
                .thenThrow(new UserServiceException("Family not found", HttpStatus.NOT_FOUND));

        CreateFamilyBudgetPlanDto requestWithInvalidFamily = CreateFamilyBudgetPlanDto.builder()
                .familyId(999L)
                .budgetLimit(5000)
                .familyMembersPlan(Arrays.asList())
                .build();

        // When & Then
        assertThatThrownBy(() -> familyBudgetPlanService.createMonthlyFamilyBudgetPlan(requestWithInvalidFamily))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Family not found");

        verify(modelFinder).findFamily(999L);
    }

    @Test
    @DisplayName("Should handle period not found exception")
    void shouldHandlePeriodNotFoundException() {
        // Given
        Long invalidPeriodId = 999L;
        when(budgetPeriodRepository.findById(invalidPeriodId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> familyBudgetPlanService.getFamilyBudgetPlanByFamilyId(1L, invalidPeriodId))
                .isInstanceOf(UserServiceException.class);

        verify(budgetPeriodRepository).findById(invalidPeriodId);
    }

    @Test
    @DisplayName("Should handle budget plan not found")
    void shouldHandleBudgetPlanNotFound() {
        // Given
        when(budgetPeriodUtil.getOrCreateCurrentBudgetPeriod()).thenReturn(testPeriod);
        when(familyBudgetPlanRepository.findByFamilyIdAndPeriodId(1L, 1L))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> familyBudgetPlanService.getFamilyBudgetPlanByFamilyId(1L, null))
                .isInstanceOf(UserServiceException.class);

        verify(budgetPeriodUtil).getOrCreateCurrentBudgetPeriod();
        verify(familyBudgetPlanRepository).findByFamilyIdAndPeriodId(1L, 1L);
    }

    @Test
    @DisplayName("Should create plan with zero member plans")
    void shouldCreatePlanWithZeroMemberPlans() {
        // Given
        CreateFamilyBudgetPlanDto requestWithNoMembers = CreateFamilyBudgetPlanDto.builder()
                .familyId(1L)
                .budgetLimit(5000)
                .familyMembersPlan(Arrays.asList())
                .build();

        when(modelFinder.findFamily(1L)).thenReturn(testFamily);
        when(budgetPeriodUtil.getOrCreateCurrentBudgetPeriod()).thenReturn(testPeriod);
        when(familyBudgetPlanRepository.save(any(FamilyBudgetPlan.class))).thenReturn(testFamilyBudgetPlan);

        // When
        FamilyBudgetPlanDto result = familyBudgetPlanService.createMonthlyFamilyBudgetPlan(requestWithNoMembers);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFamilyBudgetLimit()).isEqualTo(5000);

        verify(modelFinder).findFamily(1L);
        verify(budgetPeriodUtil).getOrCreateCurrentBudgetPeriod();
        verify(familyBudgetPlanRepository).save(any(FamilyBudgetPlan.class));
        verify(userCategoryPlanService, never()).createUserCategoryPlan(any());
    }
}