package main.service;

import main.model.Status;
import main.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StatusServiceImpl implements StatusService {

	private final Logger logger = LoggerFactory.getLogger(StatusServiceImpl.class);
	private StatusRepository statusRepository;

	@Autowired
	public StatusServiceImpl(StatusRepository statusRepository) {
		this.statusRepository = statusRepository;
	}

	@Override
	public Status get(Long number) {
		logger.debug("Searching status with id: {}", number);
		return statusRepository.getByNumberAndDeleted(number, false);
	}

	public Status getById(Long id) {
		logger.debug("Searching status with id: {}", id);
		return statusRepository.getById(id);
	}

	@Override
	public Status getByName(String name) {
		logger.debug("Searching status with name: {}", name);
		Status searchingStatus = statusRepository.getByNameAndDeleted(name, false);
		if (searchingStatus == null) {
			logger.debug("Status {} not found", name);
		}
		return searchingStatus;
	}

	@Override
	public Status getByNumber(Long statusNumber) {
		logger.debug("Searching status with number: {}", statusNumber.toString());
		Status searchingStatus = statusRepository.getByNumberAndDeleted(statusNumber, false);
		if (searchingStatus == null) {
			logger.debug("Status with number {} not found", statusNumber.toString());
		}
		return searchingStatus;
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

	@Override
	public List<Status> getAll() {
		logger.debug("Getting list statuses.");
		List<Status> listStatuses = statusRepository.getAllByDeleted(false);
		if (listStatuses.size() > 0) {
			logger.debug("The resulting list");
		} else {
			logger.debug("The list is empty");
		}
		return listStatuses;
	}
}
