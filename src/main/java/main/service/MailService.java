package main.service;

import main.model.Mail;
import main.model.Mail.MailNames;

public interface MailService {

	void sendEmail(Mail mail);

	Mail getByMailName(MailNames nameMail);

	void save(Mail mail);

}
