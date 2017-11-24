package main.service;

import main.model.Delivery;
import java.util.List;

public interface DeliveryService {

	void save(Delivery delivery);
	List<Delivery> pickupDeliveries();
	Delivery get(Long id);
}
