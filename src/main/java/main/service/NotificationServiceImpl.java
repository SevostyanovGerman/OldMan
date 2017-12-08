package main.service;

import main.model.Notification;
import main.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

	NotificationRepository notificationRepository;
	UserService userService;

	@Autowired
	public NotificationServiceImpl(NotificationRepository notificationRepository,
								   UserService userService) {
		this.notificationRepository = notificationRepository;
		this.userService = userService;
	}

	@Override
	public List<Notification> findAllByUser(String user) {
		return notificationRepository.findAllByRecipient(user);
	}

	@Override
	public void save(Notification notification) {
		notificationRepository.save(notification);
	}

	@Override
	public void delete(Long id) {
		notificationRepository.delete(id);
	}
}
