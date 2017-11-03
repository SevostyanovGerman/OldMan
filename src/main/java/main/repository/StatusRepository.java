package main.repository;

import main.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository <Status, Long> {

	@Deprecated
	Status getById(Long id);
	Status getByName(String name);
	Status getByNumber(Long number);
}
