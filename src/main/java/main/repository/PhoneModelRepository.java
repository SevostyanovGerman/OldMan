package main.repository;

import main.model.PhoneModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneModelRepository extends JpaRepository<PhoneModel, Long> {
	PhoneModel getByModelName(String name);
}
