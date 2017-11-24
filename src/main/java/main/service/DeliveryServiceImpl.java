package main.service;

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
}
