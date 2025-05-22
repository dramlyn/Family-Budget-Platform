package fbp.app.dto.goal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GoalDto {
    private Long id;
    private int balance;
    private int cost;
    private boolean isPaid;
    private String name;
    private String description;
    private Long familyId;
    private Instant createdAt;
}
