package main.repository;

import java.util.List;
import main.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findAllByRecipient(String recipient);

	void removeAllByCommentId(Long commentId);

	List<Notification> findAllByOrderIdAndRecipient(Long order, String recipient);
}
