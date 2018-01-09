package main.service;

import java.util.List;
import main.model.PhoneModel;

public interface PhoneModelService {

	List<PhoneModel> getAll();

	PhoneModel get(Long idPhoneModel);

	PhoneModel getByModelName(String modelName);

	void save(PhoneModel phoneModel);

	void delete(PhoneModel phoneModel);
}
