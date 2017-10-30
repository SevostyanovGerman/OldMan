package main.service;

import main.model.Status;

import java.util.List;

public interface StatusService {
	Status get(Long id);
	Status save(Status status);
	Status update(Status status);
	List<Status> getAll();
}
