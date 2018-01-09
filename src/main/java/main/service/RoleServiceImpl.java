package main.service;

import java.util.List;
import javax.transaction.Transactional;
import main.model.Role;
import main.model.Status;
import main.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	private final static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	private RoleRepository roleRepository;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Role getById(Long id) {
		logger.debug("Searching role with id: {}", id);
		return roleRepository.getById(id);
	}

	@Override
	public Role get(Long id) {
		logger.debug("Searching role with id: {}", id);
		return roleRepository.findOne(id);
	}

	public Role getByName(String name) {
		logger.debug("Searching role with name: {}", name);
		Role searchingRole = roleRepository.getByNameAndDeleted(name, false);
		if (searchingRole == null) {
			logger.debug("Role {} not found", name);
		}
		return searchingRole;
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
	public List<Role> getAll() {
		logger.debug("Getting list statuses.");
		List<Role> listRole = roleRepository.getAllByDeleted(false);
		if (listRole.size() > 0) {
			logger.debug("The resulting list");
		} else {
			logger.debug("The list is empty");
		}
		return listRole;
	}

	@Override
	public List<Role> getAllByStatus(Status status) {
		return roleRepository.getAllByStatuses(status);
	}
}
