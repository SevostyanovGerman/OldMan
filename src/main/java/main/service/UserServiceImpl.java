package main.service;

import main.model.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    
    @Override
    public User get(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public User getName(String name) {
        return userRepository.findUserByNameAndDeletedAndDisable(name, 0, 0);
    }
}
