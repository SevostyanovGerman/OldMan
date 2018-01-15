package main.repository;

import java.util.List;
import main.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Payment findByNameAndDeleted(String name, boolean deleted);

	List<Payment> findAllByDeleted(boolean deleted);

}
