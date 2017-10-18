package main.service;

import main.model.Status;
import main.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class StatusServiceImpl implements StatusService {
	@Autowired
	private StatusRepository statusRepository;

	@Override
	public Status get(Long id) {
		return statusRepository.getById(id);
	}
}
