package main.service;

import main.model.Role;
import main.model.User;
import main.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Override
	public User get(Long id) {
		logger.debug("Searching user with id: {}", id);
		return userRepository.getOne(id);
	}

	@Override
	public User getByName(String name) {
		logger.debug("Searching user with name: {}", name);
		return userRepository.findUserByNameAndDeletedAndDisable(name, false, false);
	}

	@Override
	public User save(User user) {
		logger.debug("Save user {}", user.toString());
		return userRepository.saveAndFlush(user);
	}

	@Override
	public User update(User user) {
		logger.debug("Update user {}", user.toString());
		return userRepository.saveAndFlush(user);
	}

	@Override
	public User getByEmail(String email){
		logger.debug("Search users by email {}", email);
		User user = userRepository.findUserByEmailAndDeleted(email, false);
		if (user == null){
			logger.debug("User with email {} was not found", email);
		}
		return  user;
	}

	@Override
	public List<User> getByRole(Long roleId) {
		logger.debug("Search users by role {}", roleId);
		Role role = roleService.get(roleId);
		if (role == null) {
			logger.debug("Role {} was not found", roleId);
		}
		return userRepository.getAllByRoles(role);
	}

	@Override
 	public List <User> getByRoleName(String roleName) {
		Role role = roleService.getByName(roleName);
		return userRepository.getAllByRoles(role);
	}

	public List<User> getAllUsers() {
		logger.debug("Getting all users");
		List <User> listUsers = userRepository.getAllByDeleted(false);
		if (listUsers.size() > 0) {
			logger.debug("The resulting list");
		} else {
			logger.debug("The list is empty");
		}
		return listUsers;
	}

	@Override
	public User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
