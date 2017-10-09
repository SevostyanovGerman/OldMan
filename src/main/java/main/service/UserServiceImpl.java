package main.service;

import main.model.Role;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;
    
    @Override
    public User get(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public User getByName(String name) {
        return userRepository.findUserByNameAndDeletedAndDisable(name, 0, 0);
    }

    @Override
    public List<User> getByRole(String name) {
        Role role = roleService.getByname(name);
        return userRepository.getAllByRoles(role);
    }
}
