package main.service;

import java.util.List;
import main.model.Notification;

public interface NotificationService {

	List<Notification> findAllByUser(String user);

	void save(Notification notification);

	void delete(Long id);

	void removeAllByCommentId(Long commentId);

	List<Notification> getByUserAndOrder(Long order, String user);
}
