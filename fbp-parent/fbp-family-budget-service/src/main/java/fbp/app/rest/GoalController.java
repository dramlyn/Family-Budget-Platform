package fbp.app.rest;

import fbp.app.dto.goal.CreateGoalDtoRequest;
import fbp.app.dto.goal.GoalDto;
import fbp.app.dto.goal.TopUpGoalBalanceDtoRequest;
import fbp.app.dto.goal.UpdateGoalDto;
import fbp.app.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/goal")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PARENT')")
    public ResponseEntity<GoalDto> createGoal(@RequestBody @Valid CreateGoalDtoRequest goalDto) {
        return ResponseEntity.ok(goalService.createGoal(goalDto));
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<GoalDto> getGoal(@PathVariable Long goalId) {
        return ResponseEntity.ok(goalService.getGoalById(goalId));
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<List<GoalDto>> getAllFamilyGoals(@PathVariable Long familyId) {
        return ResponseEntity.ok(goalService.getAllFamilyGoals(familyId));
    }

    @DeleteMapping("/{goalId}")
    @PreAuthorize("hasRole('ROLE_PARENT')")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoalById(goalId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{goalId}")
    @PreAuthorize("hasRole('ROLE_PARENT')")
    public ResponseEntity<GoalDto> updateGoal(@PathVariable Long goalId,
                                              @RequestBody UpdateGoalDto request){
        return ResponseEntity.ok(goalService.updateGoal(goalId, request));
    }

    @PatchMapping
    public ResponseEntity<GoalDto> topUpBalance(@RequestBody @Valid TopUpGoalBalanceDtoRequest request){
        return goalService.topUpGoalBalance(request);
    }
}
