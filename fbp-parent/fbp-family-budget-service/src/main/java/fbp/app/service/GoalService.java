package fbp.app.service;

import fbp.app.dto.goal.TopUpGoalBalanceDtoRequest;
import fbp.app.dto.goal.UpdateGoalDto;
import fbp.app.exception.UserServiceException;
import fbp.app.dto.goal.CreateGoalDtoRequest;
import fbp.app.dto.goal.GoalDto;
import fbp.app.mapper.GoalMapper;
import fbp.app.model.Family;
import fbp.app.model.Goal;
import fbp.app.repository.GoalRepository;
import fbp.app.util.ModelFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoalService {
    private final GoalRepository goalRepository;
    private final ModelFinder modelFinder;

    @Transactional
    public GoalDto createGoal(CreateGoalDtoRequest goalRequest) {
        Family family = modelFinder.findFamily(goalRequest.getFamilyId());
        Goal goal = GoalMapper.toGoal(goalRequest, family);

        try {
            return GoalMapper.toGoalDto(goalRepository.save(goal));
        } catch (Exception e){
            String message = "Error while saving goal: %s".formatted(e.getMessage());
            throw new UserServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public GoalDto getGoalById(Long goalId) {
        Goal goal = goalRepository
                .findById(goalId)
                .orElseThrow(() -> new UserServiceException("Goal with id %s not found".formatted(goalId), HttpStatus.NOT_FOUND));
        return GoalMapper.toGoalDto(goal);
    }

    public List<GoalDto> getAllFamilyGoals(Long familyId) {
        modelFinder.findFamily(familyId);
        return GoalMapper.toGoalDto(goalRepository.findByFamilyId(familyId));
    }

    @Transactional
    public void deleteGoalById(Long goalId) {
        try {
            goalRepository.deleteById(goalId);
        } catch (Exception e){
            String message = "Error while deleting goal: %s".formatted(e.getMessage());
            log.error(message, e);
            throw new UserServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public GoalDto updateGoal(Long goalId, UpdateGoalDto updateGoalDto){
        Goal goal = modelFinder.findGoal(goalId);

        goal.setName(updateGoalDto.getName());
        goal.setDescription(updateGoalDto.getDescription());

        return GoalMapper.toGoalDto(goalRepository.save(goal));
    }

    @Transactional
    public ResponseEntity<GoalDto> topUpGoalBalance(TopUpGoalBalanceDtoRequest request) {
        Goal goal = modelFinder.findGoal(request.getGoalId());
        if(goal.getIsPaid()){
            throw new UserServiceException("Goal with id %s is already paid.".formatted(goal.getId()), HttpStatus.BAD_REQUEST);
        }

        int currentSum = goal.getBalance() + request.getBalance();

        if(currentSum > goal.getCost()){
            String message = "No extra money can be added to the goal balance with id %s".formatted(request.getGoalId());
            throw new UserServiceException(message, HttpStatus.BAD_REQUEST);
        } else if (currentSum == goal.getCost()){
            goal.setBalance(currentSum);
            goal.setIsPaid(true);
        } else {
            goal.setBalance(currentSum);
        }

        return ResponseEntity.ok(GoalMapper.toGoalDto(goalRepository.save(goal)));
    }
}
