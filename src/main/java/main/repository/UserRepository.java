package main.repository;

import main.model.Role;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository <User, Long> {

	User findUserByEmailAndDeleted(String email, boolean del);
	User findUserByNameAndDeletedAndDisable(String name, boolean del, boolean disable);
	List <User> getAllByRoles(Role role);
	List <User> getAllByDeleted(boolean del);
}
