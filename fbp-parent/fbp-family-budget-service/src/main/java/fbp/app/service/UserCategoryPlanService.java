package fbp.app.service;

import fbp.app.dto.family_budget_plan.CreateUserCategoryPlanDtoRequest;
import fbp.app.dto.family_budget_plan.UserCategoryPlanDto;
import fbp.app.dto.user_budget.SumUserBudgetDto;
import fbp.app.dto.user_budget.UserBudgetDto;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.UserCategoryPlanMapper;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.Category;
import fbp.app.model.User;
import fbp.app.model.UserCategoryPlan;
import fbp.app.repository.UserCategoryPlanRepository;
import fbp.app.util.BudgetPeriodUtil;
import fbp.app.util.ModelFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCategoryPlanService {
    private final UserCategoryPlanRepository userCategoryPlanRepository;
    private final ModelFinder modelFinder;
    private final BudgetPeriodUtil budgetPeriodUtil;

    @Transactional
    public UserCategoryPlanDto createUserCategoryPlan(CreateUserCategoryPlanDtoRequest request){
        User user = modelFinder.findUserById(request.getUserId());
        Category category = modelFinder.findCategory(request.getCategoryId());
        BudgetPeriod budgetPeriod = budgetPeriodUtil.getOrCreateCurrentBudgetPeriod();

        UserCategoryPlan userCategoryPlan = UserCategoryPlanMapper.toModel(request, category, user, budgetPeriod);

        try {
            return UserCategoryPlanMapper.toDto(userCategoryPlanRepository.save(userCategoryPlan));
        } catch (Exception e){
            String message = "Fail when saving user category plan for user %s, category %s".formatted(request.getUserId(), request.getCategoryId());
            log.error(message);
            throw new UserServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public UserCategoryPlanDto getUserCategoryPlan(Long id){
        UserCategoryPlan plan = userCategoryPlanRepository.findById(id).orElseThrow(() -> new UserServiceException(
                "User category plan with id %s not found".formatted(id), HttpStatus.NOT_FOUND
        ));
        return UserCategoryPlanMapper.toDto(plan);
    }

    public List<UserCategoryPlanDto> findByFamilyIdAndBudgetPeriod(Long familyId, Long periodId){
        return userCategoryPlanRepository.findByFamilyIdAndPeriodId(periodId, familyId).stream()
                .map(UserCategoryPlanMapper::toDto).toList();
    }

    public SumUserBudgetDto getUserMonthSummaryBudget(String kkId, Long periodId){
        BudgetPeriod budgetPeriod = budgetPeriodUtil.getByPeriodIdOrLatestPeriodId(periodId);
        User user = modelFinder.findUserByKeycloakId(kkId);

        List<UserCategoryPlan> userCategoriesBalances = userCategoryPlanRepository.findByUserIdAndPeriodId(user.getId(), budgetPeriod.getId());

        return SumUserBudgetDto.builder()
                .userId(user.getId())
                .periodId(budgetPeriod.getId())
                .budget(userCategoriesBalances.stream().mapToInt(UserCategoryPlan::getLimit).sum())
                .build();

    }

    public UserBudgetDto getUserCategoryBudget(String kkId, Long periodId, Long categoryId){
        BudgetPeriod budgetPeriod = budgetPeriodUtil.getByPeriodIdOrLatestPeriodId(periodId);
        Category category = modelFinder.findCategory(categoryId);
        User user = modelFinder.findUserByKeycloakId(kkId);

        UserCategoryPlan userCategoryPlan = userCategoryPlanRepository.findByCategoryIdAndPeriodIdAndUserId(
                category.getId(), budgetPeriod.getId(), user.getId())   
                .orElseThrow(() -> new UserServiceException("User category plan not found by categoryId %s, periodId %s, userId %s".formatted(category.getId(),
                        budgetPeriod.getId(), user.getId()), HttpStatus.NOT_FOUND));

        return UserBudgetDto.builder()
                .budget(userCategoryPlan.getLimit())
                .categoryId(category.getId())
                .periodId(budgetPeriod.getId())
                .userId(user.getId())
                .build();
    }
}
