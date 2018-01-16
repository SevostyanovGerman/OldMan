package main.service;

import java.util.List;
import javax.transaction.Transactional;
import main.model.Notification;
import main.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

	private NotificationRepository notificationRepository;

	@Autowired
	public NotificationServiceImpl(NotificationRepository notificationRepository, UserService userService) {
		this.notificationRepository = notificationRepository;
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

	@Transactional
	@Override
	public void removeAllByCommentId(Long commentId) {
		notificationRepository.removeAllByCommentId(commentId);
	}

	@Override
	public List<Notification> getByUserAndOrder(Long order, String user) {
		return notificationRepository.findAllByOrderIdAndRecipient(order, user);
	}
}
