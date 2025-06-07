package fbp.app.rest;

import fbp.app.dto.family_budget_plan.CreateFamilyBudgetPlanDto;
import fbp.app.dto.family_budget_plan.FamilyBudgetPlanDto;
import fbp.app.service.FamilyBudgetPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/plan")
@RequiredArgsConstructor
public class FamilyBudgetPlanController {
    private final FamilyBudgetPlanService familyBudgetPlanService;

    @PostMapping
    public ResponseEntity<?> createMonthlyFamilyBudgetPlan(@RequestBody @Valid CreateFamilyBudgetPlanDto request){
        return ResponseEntity.ok(familyBudgetPlanService.createMonthlyFamilyBudgetPlan(request));
    }

    @GetMapping
    public ResponseEntity<FamilyBudgetPlanDto> getFamilyBudgetPlanByParams(@RequestParam(required = false) Long periodId,
                                                                           @RequestParam Long familyId){
        return ResponseEntity.ok(familyBudgetPlanService.getFamilyBudgetPlanByFamilyId(familyId, periodId));
    }
}
