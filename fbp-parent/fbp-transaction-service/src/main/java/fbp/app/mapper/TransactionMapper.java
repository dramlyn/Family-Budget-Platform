package fbp.app.mapper;

import fbp.app.dto.CreateTransactionDtoRequest;
import fbp.app.dto.TransactionDto;
import fbp.app.model.BudgetPeriod;
import fbp.app.model.Category;
import fbp.app.model.Transaction;
import fbp.app.model.User;

import java.time.Instant;

public class TransactionMapper {
    public static Transaction toModel(CreateTransactionDtoRequest request, Category category, User user,
                                      BudgetPeriod budgetPeriod, Instant createdAt){
        return Transaction.builder()
                .amount(request.getAmount())
                .user(user)
                .category(category)
                .type(request.getType())
                .period(budgetPeriod)
                .createdAt(createdAt)
                .build();
    }

    public static TransactionDto toDto(Transaction transaction){
        return TransactionDto.builder()
                .id(transaction.getId())
                .categoryId(transaction.getCategory().getId())
                .userId(transaction.getUser().getId())
                .periodId(transaction.getPeriod().getId())
                .createdAt(transaction.getCreatedAt())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .build();
    }
}
