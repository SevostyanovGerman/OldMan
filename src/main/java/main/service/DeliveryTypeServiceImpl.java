package main.service;

import main.model.DeliveryType;
import main.repository.DeliveryTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DeliveryTypeServiceImpl implements DeliveryTypeService {

	private final DeliveryTypeRepository deliveryTypeRepository;

	@Autowired
	public DeliveryTypeServiceImpl(DeliveryTypeRepository deliveryTypeRepository) {
		this.deliveryTypeRepository = deliveryTypeRepository;
	}

	@Override
	public List<DeliveryType> getAll() {
		return deliveryTypeRepository.findAll();
	}

	@Override
	public void save(DeliveryType deliveryType) {
		deliveryTypeRepository.save(deliveryType);
	}

	@Override
	public DeliveryType get(Long id) {
		return deliveryTypeRepository.getOne(id);
	}
}
