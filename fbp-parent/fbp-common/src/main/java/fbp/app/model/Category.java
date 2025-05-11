package fbp.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(name = "category", schema = "fbp")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
}
