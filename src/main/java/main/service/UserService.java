package main.service;

import main.model.User;
import java.util.List;

public interface UserService {
	User get(Long id);
	User getByName(String name);
	User save(User user);
	User update(User user);
	List <User> getByRole(String role);
	List <User> getAllUsers();
}
