package fbp.app.rest;

import fbp.app.dto.user_budget.SumUserBudgetDto;
import fbp.app.dto.user_budget.UserBudgetDto;
import fbp.app.service.UserCategoryPlanService;
import fbp.app.util.FamilyBudgetPlatformUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user-budget")
@RequiredArgsConstructor
public class UserBudgetController {
    private final UserCategoryPlanService userCategoryPlanService;
    private final FamilyBudgetPlatformUtils familyBudgetPlatformUtils;

    @GetMapping
        public ResponseEntity<SumUserBudgetDto> getMonthSumUserBalance(@RequestParam Long periodId){
        return ResponseEntity.ok(userCategoryPlanService.getUserMonthSummaryBudget(
                familyBudgetPlatformUtils.getCurrentUserKkId(), periodId));
    }

    @GetMapping("/by-param")
    public ResponseEntity<UserBudgetDto> getMonthSumUserBalance(@RequestParam Long periodId,
                                                                @RequestParam Long categoryId){
        return ResponseEntity.ok(userCategoryPlanService.getUserCategoryBudget(
                familyBudgetPlatformUtils.getCurrentUserKkId(), periodId, categoryId));
    }
}
