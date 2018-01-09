package main.service;

import java.util.List;
import main.model.Customer;
import main.model.Delivery;

public interface DeliveryService {

	void save(Delivery delivery);

	List<Delivery> pickupDeliveries();

	Delivery get(Long id);

	Delivery checkContainsDeliveryInCustomer(Customer customer, Delivery delivery);
}
