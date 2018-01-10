package main.service;

import main.model.Comment;
import main.model.User;

public interface MailService {

	void sendResetPasswordMail(User user);

	void sendNotificationByMail(User user, Comment comment);

}
