package main.service;

import main.model.Role;
import java.util.List;

public interface RoleService {
	Role get(Long id);
	Role getByName(String name);
	Role save(Role role);
	Role update(Role role);
	List<Role> getAll();
}
