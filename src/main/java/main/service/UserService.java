package main.service;

import main.model.User;

import java.util.List;

public interface UserService {
     User get (Long id);
     User getByName (String name);
     List<User> getByRole ( String role);
}
