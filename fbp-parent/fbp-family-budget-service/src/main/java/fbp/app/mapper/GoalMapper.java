package fbp.app.mapper;

import fbp.app.dto.goal.CreateGoalDtoRequest;
import fbp.app.dto.goal.GoalDto;
import fbp.app.model.Family;
import fbp.app.model.Goal;

import java.time.Instant;
import java.util.List;

public class GoalMapper {
    public static Goal toGoal(CreateGoalDtoRequest request, Family family){
        return Goal.builder()
                .cost(request.getCost())
                .family(family)
                .balance(0)
                .isPaid(false)
                .createdAt(Instant.now())
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public static GoalDto toGoalDto(Goal goal){
        return GoalDto.builder()
                .id(goal.getId())
                .balance(goal.getBalance())
                .cost(goal.getCost())
                .name(goal.getName())
                .isPaid(goal.getIsPaid())
                .description(goal.getDescription())
                .familyId(goal.getFamily().getId())
                .createdAt(goal.getCreatedAt())
                .build();
    }

    public static List<GoalDto> toGoalDto(List<Goal> goal){
        return goal.stream().map(GoalMapper::toGoalDto).toList();
    }
}
