package main.service;

import main.model.PhoneModel;
import main.repository.PhoneModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PhoneModelServiceImpl implements PhoneModelService {

	private PhoneModelRepository modelRepository;

	@Autowired
	public PhoneModelServiceImpl(PhoneModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	@Override
	public List<PhoneModel> getAll() {
		return modelRepository.findAll();
	}

	@Override
	public PhoneModel get(Long idPhoneModel) {
		return modelRepository.getOne(idPhoneModel);
	}

	@Override
	public PhoneModel getByName(String name) {
		return modelRepository.getByModelName(name);
	}


	@Override
	public void save(PhoneModel phoneModel) {
		modelRepository.saveAndFlush(phoneModel);
	}

	@Override
	public void delete(PhoneModel phoneModel) {
		modelRepository.delete(phoneModel);
	}
}
