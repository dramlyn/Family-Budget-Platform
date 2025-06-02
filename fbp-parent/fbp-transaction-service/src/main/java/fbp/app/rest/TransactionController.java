package fbp.app.rest;

import fbp.app.dto.CreateTransactionDtoRequest;
import fbp.app.dto.TransactionDto;
import fbp.app.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Создание новой транзакции")
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody CreateTransactionDtoRequest request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Получить транзакцию по id")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }

    @GetMapping("/by-param")
    @Operation(summary = "Получить транзакцию по параметрам (periodId)")
    public ResponseEntity<List<TransactionDto>> getTransactionByParam(@RequestParam(name = "periodId", required = false) Long periodId) {
        return ResponseEntity.ok(transactionService.getAllByParam(periodId));
    }

    @GetMapping("/by-category/{categoryId}")
    @Operation(summary = "Получить транзакцию по категории и периоду")
    public ResponseEntity<List<TransactionDto>> getTransactionsByCategoryId(@PathVariable Long categoryId,
                                                                            @RequestParam Long periodId){
        return ResponseEntity.ok(transactionService.getAllByCategoryIdAndPeriodId(categoryId, periodId));
    }

    @DeleteMapping("/{transactionId}")
    @Operation(summary = "Удалить транзакцию по id")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransactionById(transactionId);
        return ResponseEntity.noContent().build();
    }
}
