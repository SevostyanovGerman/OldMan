package main.service;

import java.util.List;
import javax.transaction.Transactional;
import main.model.PhoneModel;
import main.repository.PhoneModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PhoneModelServiceImpl implements PhoneModelService {

	private final static Logger logger = LoggerFactory.getLogger(PhoneModelServiceImpl.class);
	private PhoneModelRepository modelRepository;

	@Autowired
	public PhoneModelServiceImpl(PhoneModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	@Override
	public List<PhoneModel> getAll() {
		logger.debug("Getting list of phone models.");
		List<PhoneModel> listModels = modelRepository.findAllByDeleted(false);
		if (listModels.size() > 0) {
			logger.debug("The resulting list");
		} else {
			logger.debug("The list is empty");
		}
		return listModels;
	}

	@Override
	public PhoneModel get(Long idPhoneModel) {
		logger.debug("Searching model phone with id: {}", idPhoneModel);
		return modelRepository.getOne(idPhoneModel);
	}

	@Override
	public PhoneModel getByModelName(String modelName) {
		logger.debug("Searching model phone with name: {}", modelName);
		PhoneModel searchingModel = modelRepository.getByModelNameAndDeleted(modelName, false);
		if (searchingModel == null) {
			logger.debug("Product {} not found", modelName);
		}
		return searchingModel;
	}

	@Override
	public void save(PhoneModel phoneModel) {
		logger.debug("Save phone model: {}", phoneModel.toString());
		modelRepository.saveAndFlush(phoneModel);
	}

	@Override
	public void delete(PhoneModel phoneModel) {
		logger.debug("Delete phone model: {}", phoneModel.toString());
		phoneModel.setDeleted(true);
		modelRepository.save(phoneModel);
	}
}
