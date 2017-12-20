package main.service;

import main.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;

@Component
public class MailServiceImpl implements MailService {

	@Autowired
	private MailContentBuilder mailContentBuilder;

	@Autowired
	private JavaMailSender javaMailSender;

	private final static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Override
	public void sendEmail(String title, String content, User user, String template) {
		try {
			MimeMessage mail = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			helper.setTo(user.getEmail());
			helper.setSubject(title);
			content = mailContentBuilder.build(content, user, template);
			helper.setText(content, true);
			try {
				javaMailSender.send(mail);
			} catch (Exception e) {
				logger.error("MailServer connection error");
			}
		} catch (Exception e) {
			logger.error("While sending mail");
		}
	}
}

