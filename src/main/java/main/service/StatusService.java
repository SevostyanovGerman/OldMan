package main.service;

import main.model.Status;

public interface StatusService {

	Status get(Long id);
	Status getByName(String name);
	Status save(Status status);
	Status update(Status status);
}
