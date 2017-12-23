package main.service;

import main.model.PhoneModel;

import java.util.List;

public interface PhoneModelService {

	List<PhoneModel> getAll();
	PhoneModel get(Long idPhoneModel);
	PhoneModel getByName(String name);
	void save(PhoneModel phoneModel);
	void delete(PhoneModel phoneModel);
}
