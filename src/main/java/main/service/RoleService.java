package main.service;

import java.util.List;
import main.model.Role;
import main.model.Status;

public interface RoleService {

	Role get(Long id);

	Role getById(Long id);

	Role getByName(String name);

	Role save(Role role);

	Role update(Role role);

	List<Role> getAll();

	List<Role> getAllByStatus(Status status);
}
