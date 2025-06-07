package fbp.app.mapper;

import fbp.app.dto.family_budget_plan.CreateUserCategoryPlanDtoRequest;
import fbp.app.dto.family_budget_plan.UserCategoryPlanDto;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.Category;
import fbp.app.model.User;
import fbp.app.model.UserCategoryPlan;

public class UserCategoryPlanMapper {
    public static UserCategoryPlan toModel(CreateUserCategoryPlanDtoRequest request,
                                           Category category, User user, BudgetPeriod period){
        return UserCategoryPlan.builder()
                .period(period)
                .user(user)
                .category(category)
                .limit(request.getLimit())
                .build();
    }

    public static UserCategoryPlanDto toDto(UserCategoryPlan userCategoryPlan){
        return UserCategoryPlanDto.builder()
                .id(userCategoryPlan.getId())
                .userId(userCategoryPlan.getUser().getId())
                .categoryId(userCategoryPlan.getCategory().getId())
                .periodId(userCategoryPlan.getPeriod().getId())
                .limit(userCategoryPlan.getLimit())
                .build();
    }
}
