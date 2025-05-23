package fbp.app.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryExpenseReportDto {
    private int expense;
    private Long categoryId;
    private Long userId;
}
