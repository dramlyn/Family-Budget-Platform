package fbp.app.dto;

import fbp.app.model.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionDto {
    private Long id;
    private TransactionType type;
    private Integer amount;
    private Long categoryId;
    private Long userId;
    private Long periodId;
    private Instant createdAt;
}
