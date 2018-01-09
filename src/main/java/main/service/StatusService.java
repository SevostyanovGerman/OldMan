package main.service;

import java.util.List;
import main.model.Status;

public interface StatusService {

	Status get(Long number);

	Status getById(Long id);

	Status getByName(String name);

	Status getByNumber(Long statusNumber);

	Status save(Status status);

	Status update(Status status);

	List<Status> getAll();
}
