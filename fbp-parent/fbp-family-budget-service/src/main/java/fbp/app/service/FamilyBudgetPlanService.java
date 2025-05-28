package fbp.app.service;

import fbp.app.dto.family_budget_plan.CreateFamilyBudgetPlanDto;
import fbp.app.dto.family_budget_plan.CreateUserCategoryPlanDtoRequest;
import fbp.app.dto.family_budget_plan.FamilyBudgetPlanDto;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.FamilyBudgetPlanMapper;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.Family;
import fbp.app.model.FamilyBudgetPlan;
import fbp.app.repository.BudgetPeriodRepository;
import fbp.app.repository.FamilyBudgetPlanRepository;
import fbp.app.util.BudgetPeriodUtil;
import fbp.app.util.ModelFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FamilyBudgetPlanService {
    private final FamilyBudgetPlanRepository familyBudgetPlanRepository;
    private final UserCategoryPlanService userCategoryPlanService;
    private final ModelFinder modelFinder;
    private final BudgetPeriodUtil budgetPeriodUtil;
    private final BudgetPeriodRepository budgetPeriodRepository;

    @Transactional
    public FamilyBudgetPlanDto createMonthlyFamilyBudgetPlan(CreateFamilyBudgetPlanDto request){
        Family family = modelFinder.findFamily(request.getFamilyId());
        BudgetPeriod period = budgetPeriodUtil.getOrCreateCurrentBudgetPeriod();

        if(request.getBudgetLimit() < request.getFamilyMembersPlan().stream().mapToInt(CreateUserCategoryPlanDtoRequest::getLimit).sum()){
            throw new UserServiceException("Family members limit sum more than family budget limit.", HttpStatus.BAD_REQUEST);
        }

        request.getFamilyMembersPlan().forEach(userCategoryPlanService::createUserCategoryPlan);
        FamilyBudgetPlan familyBudgetPlan = FamilyBudgetPlanMapper.toModel(request, family, period);
        return FamilyBudgetPlanMapper.toDto(familyBudgetPlanRepository.save(familyBudgetPlan));
    }

    public FamilyBudgetPlanDto getFamilyBudgetPlanByFamilyId(Long familyId, Long periodId){
        BudgetPeriod budgetPeriod = budgetPeriodUtil.getByPeriodIdOrLatestPeriodId(periodId);
        modelFinder.findFamily(familyId);

        FamilyBudgetPlan familyBudgetPlan = familyBudgetPlanRepository.findByFamilyIdAndPeriodId(familyId, budgetPeriod.getId())
                .orElseThrow(() -> new UserServiceException("Family budget plan with family id %s and period id %s not found".formatted(familyId, budgetPeriod.getId()),
                        HttpStatus.NOT_FOUND));
        return FamilyBudgetPlanMapper.toDto(familyBudgetPlan);
    }
}
