package fbp.app.util;

import fbp.app.exception.UserServiceException;
import fbp.app.model.*;
import fbp.app.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ModelFinder {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FamilyRepository familyRepository;
    private final GoalRepository goalRepository;
    private final BudgetPeriodRepository budgetPeriodRepository;

    public User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String message = String.format("User with id %s not found", userId);
                    log.error(message);
                    return new UserServiceException(message, HttpStatus.BAD_REQUEST);
                });
    }

    public User findUserByKeycloakId(String kkId){
        return userRepository.findByKeycloakId(kkId)
                .orElseThrow(() -> {
                    String message = String.format("User with keycloak id %s not found", kkId);
                    log.error(message);
                    return new UserServiceException(message, HttpStatus.BAD_REQUEST);
                });
    }

    public Category findCategory(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    String message = String.format("Category with id %s not found", categoryId);
                    log.error(message);
                    return new UserServiceException(message, HttpStatus.BAD_REQUEST);
                });
    }

    public Family findFamily(Long familyId){
        return familyRepository.findById(familyId)
                .orElseThrow(() -> {
                    String message = String.format("Family with id %s not found", familyId);
                    log.error(message);
                    return new UserServiceException(message, HttpStatus.BAD_REQUEST);
                });
    }

    public Goal findGoal(Long goalId){
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new UserServiceException("Goal with id %s not found".formatted(goalId), HttpStatus.NOT_FOUND));
    }

    public BudgetPeriod findBudgetPeriod(Long periodId){
        return budgetPeriodRepository
                .findById(periodId)
                .orElseThrow(() -> new UserServiceException("Budget period with id %s not found".formatted(periodId), HttpStatus.NOT_FOUND));
    }
}
