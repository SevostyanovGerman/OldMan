package main.service;

import main.model.User;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;
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

	List<User> getUsersByNameLike(String name);

	void addAvatar(MultipartFile avatar, User user) throws IOException, SQLException;

	User getByToken(String token);

	void setPasswordEncoder(User user, String password);
}
