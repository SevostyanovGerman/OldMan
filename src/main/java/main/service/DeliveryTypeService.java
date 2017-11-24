package main.service;

import main.model.DeliveryType;
import java.util.List;

public interface DeliveryTypeService {

	List<DeliveryType> getAll();
	void save(DeliveryType deliveryType);
	DeliveryType get(Long id);
}
