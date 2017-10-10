package main.service;

import main.model.User;
import main.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;
    
    @Override
    public User get(Long id) {
        LOGGER.debug("Searching user with id: {}", id);
        return userRepository.getOne(id);
    }

    @Override
    public User getName(String name) {
        LOGGER.debug("Searching user with name: {}", name);
        return userRepository.findUserByName(name);
    }
}
