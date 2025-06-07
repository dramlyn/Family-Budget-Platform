package fbp.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "family_budget_plan", schema = "fbp")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FamilyBudgetPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @Column(name = "\"limit\"", nullable = false)
    private Integer limit;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private BudgetPeriod period;
}
