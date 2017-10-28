package main.service;

import main.model.Status;
import main.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class StatusServiceImpl implements StatusService {

	private final Logger logger = LoggerFactory.getLogger(StatusServiceImpl.class);

	@Autowired
	private StatusRepository statusRepository;

	@Override
	public Status get(Long id) {
		logger.debug("Searching status with id: {}", id);
		return statusRepository.getById(id);
	}

	@Override
	public Status getByName(String name) {
		return statusRepository.getByName(name);
	}

	@Override
	public Status save(Status status) {
		logger.debug("Save status: {}", status.toString());
		return statusRepository.save(status);
	}

	@Override
	public Status update(Status status) {
		logger.debug("Update status: {}", status.toString());
		return statusRepository.saveAndFlush(status);
	}
}
