package main.service;

import main.model.Role;
import main.model.User;
import main.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        return userRepository.findUserByNameAndDeletedAndDisable(name, 0, 0);
    }

    @Override
    public List<User> getByRole(String name) {
        Role role = roleService.getByname(name);
        return userRepository.getAllByRoles(role);
    }
}
