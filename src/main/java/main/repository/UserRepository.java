package main.repository;

import java.util.List;
import main.model.Role;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	User findUserByEmailAndDeleted(String email, boolean del);

	User findUserByNameAndDeletedAndDisable(String name, boolean del, boolean disable);

	List<User> getAllByRolesAndDeleted(Role role, boolean deleted);

	List<User> getAllByDeleted(boolean del);

	User findByToken(String token);

	@Query("SELECT u FROM User u WHERE u.name LIKE LOWER(CONCAT('%',:name, '%'))")
	List<User> getAllByNameLike(@Param("name") String name);
}
