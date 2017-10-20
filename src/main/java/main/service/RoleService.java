package main.service;

import main.model.Role;
import java.util.List;

public interface RoleService {
     Role getByname(String name);
     List<Role> getAll();
}
