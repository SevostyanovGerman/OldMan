package main.service;

import main.model.User;

public interface UserService {
    public User get (Long id);
    public User getName (String name);
}
