package main.service;

import main.model.Role;
import main.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public Role getByname(String name) {
        logger.debug("Searching role with name: {}", name);
        return roleRepository.findByName(name);
    }
}
