package main.service;

import main.model.Notification;

import java.util.List;

public interface NotificationService {
	List<Notification> findAllByUser(String user);

	void save(Notification notification);

	void delete(Long id);
}
