package main.service;

import main.model.Role;
import main.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Override
	public Role get(Long id) {
		logger.debug("Searching role with id: {}", id);
		return roleRepository.findOne(id);
	}

	public Role save(Role role) {
		logger.debug("Save role with name: {}", role.getName());
		return roleRepository.saveAndFlush(role);
	}

	@Override
	public Role update(Role role) {
		logger.debug("Updating role with name: {}", role.getName());
		return roleRepository.saveAndFlush(role);
	}

	@Override
	public List <Role> getAll() {
		return roleRepository.findAll();
	}
}
