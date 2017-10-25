package main.repository;

import main.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository <Delivery, Long> {

}
