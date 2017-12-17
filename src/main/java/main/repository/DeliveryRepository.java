package main.repository;

import main.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

	List<Delivery> findAllByPickup(boolean pickup);
}
