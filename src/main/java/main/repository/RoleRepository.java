package main.repository;

import java.util.List;
import main.model.Role;
import main.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role getById(Long id);

	Role getByNameAndDeleted(String name, boolean del);

	List<Role> getAllByDeleted(boolean del);

	List<Role> getAllByStatuses(Status status);
}
