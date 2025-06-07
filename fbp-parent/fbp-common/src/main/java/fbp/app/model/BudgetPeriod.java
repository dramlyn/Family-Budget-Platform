package fbp.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(name = "budget_period", schema = "fbp")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetPeriod {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "year", nullable = false)
    private Integer year;
    @Column(name = "month", nullable = false)
    private Integer month;
}
