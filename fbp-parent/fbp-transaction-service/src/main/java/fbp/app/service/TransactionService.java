package fbp.app.service;

import fbp.app.dto.CreateTransactionDtoRequest;
import fbp.app.dto.TransactionDto;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.TransactionMapper;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.Category;
import fbp.app.model.Transaction;
import fbp.app.model.User;
import fbp.app.repository.CategoryRepository;
import fbp.app.repository.TransactionRepository;
import fbp.app.repository.UserRepository;
import fbp.app.util.BudgetPeriodUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BudgetPeriodUtil budgetPeriodUtil;

    @Transactional
    public TransactionDto createTransaction(CreateTransactionDtoRequest transactionRequest) {
        User user = findUser(transactionRequest.getUserId());
        Category category = findCategory(transactionRequest.getCategoryId());
        BudgetPeriod budgetPeriod = budgetPeriodUtil.getOrCreateCurrentBudgetPeriod();

        Transaction transaction = TransactionMapper.toModel(transactionRequest, category, user,
                budgetPeriod, Instant.now());

        return TransactionMapper.toDto(transactionRepository.save(transaction));
    }

    public TransactionDto getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(TransactionMapper::toDto)
                .orElseThrow(() -> {
                    String message = "Transaction with id %d not found".formatted(transactionId);
                    log.error(message);
                    return new UserServiceException(message, HttpStatus.NOT_FOUND);
                });
    }

    public List<TransactionDto> getAllByParam(Long periodId) {
        User user = userRepository.findByKeycloakId(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserServiceException("User with kkId %s not found".formatted(SecurityContextHolder.getContext().getAuthentication().getName()), HttpStatus.NOT_FOUND));
        return transactionRepository.findTransactionsWithParams(periodId, user.getId()).stream()
                .map(TransactionMapper::toDto).toList();
    }

    public List<TransactionDto> getAllByCategoryIdAndPeriodId(Long categoryId, Long periodId){
        User user = userRepository.findByKeycloakId(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserServiceException("User with kkId %s not found".formatted(SecurityContextHolder.getContext().getAuthentication().getName()), HttpStatus.NOT_FOUND));
        Category category = findCategory(categoryId);
        BudgetPeriod budgetPeriod = budgetPeriodUtil.getByPeriodIdOrLatestPeriodId(periodId);
        return transactionRepository.findAllUserTransactionsByCategoryAndPeriodAndUser(budgetPeriod.getId(), category.getId(), user.getId()).stream()
                .map(TransactionMapper::toDto).toList();
    }

    @Transactional
    public void deleteTransactionById(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    private User findUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String message = String.format("User with id %s not found", userId);
                    log.error(message);
                    return new UserServiceException(message, HttpStatus.BAD_REQUEST);
                });
    }

    private Category findCategory(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    String message = String.format("Category with id %s not found", categoryId);
                    log.error(message);
                    return new UserServiceException(message, HttpStatus.BAD_REQUEST);
                });
    }
}
