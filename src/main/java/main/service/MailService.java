package main.service;

import main.model.User;

public interface MailService {

	void sendResetPasswordMail(User user);

}
