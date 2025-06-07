package fbp.app.repository;

import fbp.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByCategoryId(Long categoryId);
    List<Transaction> findByPeriodId(Long periodId);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.period.id = :periodId " +
            "AND t.user.id = :userId")
    List<Transaction> findTransactionsWithParams(@Param("periodId") Long periodId,
                                                 @Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.period.id = :periodId " +
            "AND t.user.id = :userId " +
            "AND t.category.id = :categoryId " +
            "AND t.type = 'SPEND'")
    List<Transaction> findAllUserExpensesByCategoryAndPeriod(@Param("periodId") Long periodId,
                                              @Param("categoryId") Long categoryId,
                                              @Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.period.id = :periodId " +
            "AND t.user.id = :userId " +
            "AND t.category.id = :categoryId ")
    List<Transaction> findAllUserTransactionsByCategoryAndPeriodAndUser(@Param("periodId") Long periodId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("userId") Long userId);
}
