package main.service;

import main.model.Customer;
import main.model.Delivery;
import main.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

	@Autowired
	private DeliveryRepository deliveryRepository;

	@Override
	public void save(Delivery delivery) {
		deliveryRepository.save(delivery);
	}

	@Override
	public List<Delivery> pickupDeliveries() {
		return deliveryRepository.findAllByPickup(true);
	}

	@Override
	public Delivery get(Long id) {
		return deliveryRepository.getOne(id);
	}

	@Override
	public Delivery checkContainsDeliveryInCustomer(Customer customer, Delivery delivery) {
		List<Delivery> deliveryList = customer.getDeliveries();
		Delivery customerDelivery;
		for (int i = 0; i < deliveryList.size(); i++) {
			customerDelivery = deliveryList.get(i);
			if (customerDelivery.equalsWhithoutId(delivery)) {
				return customerDelivery;
			}
		}
		return delivery;
	}
}
