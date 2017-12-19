package main.service;

import main.model.User;
import javax.mail.MessagingException;
public interface MailService {

	void sendEmail(String title, String content, User user, String template) throws MessagingException;

}
