package fbp.app.dto.family;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FamilyDto {
    private Long id;
    private String name;
    private String description;
    private Instant createdAt;
}
