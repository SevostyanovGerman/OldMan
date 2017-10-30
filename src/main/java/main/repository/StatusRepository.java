package main.repository;

import main.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepository extends JpaRepository <Status, Long> {
	Status getById(Long id);
	List<Status> getAllByDeleted(int del);
}
