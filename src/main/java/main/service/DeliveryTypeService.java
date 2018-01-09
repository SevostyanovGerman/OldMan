package main.service;

import java.util.List;
import main.model.DeliveryType;

public interface DeliveryTypeService {

	List<DeliveryType> getAll();

	void save(DeliveryType deliveryType);

	DeliveryType get(Long id);
}
