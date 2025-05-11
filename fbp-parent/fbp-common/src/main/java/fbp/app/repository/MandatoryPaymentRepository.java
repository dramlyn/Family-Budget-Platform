package fbp.app.repository;

import fbp.app.model.MandatoryPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MandatoryPaymentRepository extends JpaRepository<MandatoryPayment, Long> {
}
