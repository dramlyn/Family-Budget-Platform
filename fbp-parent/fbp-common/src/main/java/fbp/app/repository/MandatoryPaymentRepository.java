package fbp.app.repository;

import fbp.app.model.MandatoryPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MandatoryPaymentRepository extends JpaRepository<MandatoryPayment, Long> {
    List<MandatoryPayment> findByFamilyId(Long familyId);
}
