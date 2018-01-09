package main.repository;

import java.util.List;
import main.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

	List<Delivery> findAllByPickup(boolean pickup);
}
