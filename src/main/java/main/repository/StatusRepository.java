package main.repository;

import main.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

	Status getById(Long id);

	Status getByName(String name);

	Status getByNumber(Long number);

	Status getByNameAndDeleted(String name, boolean delete);

	Status getByNumberAndDeleted(Long statusNumber, boolean delete);

	List<Status> getAllByDeleted(boolean del);
}
