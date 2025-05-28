package fbp.app.repository;

import fbp.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByKeycloakId(String keycloakId);
    @Query("SELECT t FROM User t " +
            "WHERE t.family.id = :familyId AND t.role = 'PARENT'")
    List<User> findParentByFamilyId(Long familyId);
    List<User> findByFamilyId(Long familyId);
}
