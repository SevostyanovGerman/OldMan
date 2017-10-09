package main.service;

import main.model.User;

import java.util.List;

public interface UserService {
    public User get (Long id);
    public User getByName (String name);
    public List<User> getByRole ( String role);
}
