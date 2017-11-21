package main.service;

import main.model.User;
import java.util.List;

public interface UserService {

	User get(Long id);
	User getByName(String name);
	User getByEmail(String email);
	User save(User user);
	User update(User user);
	List<User> getByRole(Long roleId);
	List<User> getByRoleName(String roleName);
	List<User> getAllUsers();
	User getCurrentUser();
}
