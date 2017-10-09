package main.service;

import main.model.Role;
import main.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleRepository roleRepository;
    @Override
    public Role getByname(String name) {
        return roleRepository.findByName(name);
    }
}
