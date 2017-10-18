package main.repository;


import main.model.Role;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByNameAndDeletedAndDisable(String name, int del, int disable);
    List<User> getAllByRoles(Role role);
    List<User> getAllByDeleted(int del);
}
