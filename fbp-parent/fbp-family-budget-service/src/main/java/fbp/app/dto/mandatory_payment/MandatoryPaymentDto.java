package fbp.app.dto.mandatory_payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MandatoryPaymentDto {
    private Long id;
    private String name;
    private boolean isPaid;
    private int amount;
    private Long familyId;
    private Instant createdAt;
    private Long periodId;
}
