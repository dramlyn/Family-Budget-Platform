package fbp.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_category_plan", schema = "fbp")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCategoryPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "\"limit\"", nullable = false)
    private Integer limit;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private BudgetPeriod period;
}
