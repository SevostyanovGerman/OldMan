package main.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import main.Helpers;
import main.model.Role;
import main.model.User;
import main.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserRepository userRepository;

	private RoleService roleService;

	private static final int IMG_WIDTH = 200;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
		this.userRepository = userRepository;
		this.roleService = roleService;
	}

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
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
		logger.debug("Save user {}", user.toString());
		return userRepository.saveAndFlush(user);
	}

	@Override
	public User update(User user) {
		logger.debug("Update user {}", user.toString());
		return userRepository.saveAndFlush(user);
	}

	@Override
	public User getByEmail(String email) {
		logger.debug("Search users by email {}", email);
		User user = userRepository.findUserByEmailAndDeleted(email, false);
		if (user == null) {
			logger.debug("User with email {} was not found", email);
		}
		return user;
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
	public List<User> getByRoleName(String roleName) {
		Role role = roleService.getByName(roleName);
		return userRepository.getAllByRoles(role);
	}

	public List<User> getAllUsers() {
		logger.debug("Getting all users");
		List<User> listUsers = userRepository.getAllByDeleted(false);
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

	@Override
	public List<User> getUsersByNameLike(String name) {
		return userRepository.getAllByNameLike(name);
	}

	@Override
	public void addAvatar(MultipartFile file, User user) throws IOException, SQLException {

		if (!file.isEmpty()) {
			BufferedImage resizedImage;
			BufferedImage originalImage = ImageIO.read(new BufferedInputStream(file.getInputStream()));
			if (originalImage.getWidth() > IMG_WIDTH) {
				resizedImage = Helpers.resizePicture(originalImage, originalImage.getType(), IMG_WIDTH);
			} else {
				resizedImage = originalImage;
			}
			Blob resizedFile = new SerialBlob(Helpers.convertToByteArray(resizedImage));
			user.setAvatar(resizedFile);
			save(user);
		}
	}

	@Override
	public User getByToken(String token) {
		return userRepository.findByToken(token);
	}

	@Override
	public void setPasswordEncoder(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
	}
}
