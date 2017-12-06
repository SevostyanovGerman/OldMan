package main.service;

import main.model.Status;
import java.util.List;

public interface StatusService {

	Status get(Long number);
	Status getById(Long id);
	Status getByName(String name);
	Status getByNumber(Long statusNumber);
	Status save(Status status);
	Status update(Status status);
	List<Status> getAll();
}
