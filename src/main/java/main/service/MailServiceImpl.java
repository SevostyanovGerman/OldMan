package main.service;

import javax.mail.internet.MimeMessage;

import main.model.Comment;
import main.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Configuration
@PropertySource("mail.properties")
public class MailServiceImpl implements MailService {

	private MailContentBuilder mailContentBuilder;
	private JavaMailSender javaMailSender;
	private final static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Value("${titleResetPassword}")
	private String titleResetPassword;

	@Value("${titleNotification}")
	private String titleNotification;

	@Autowired
	public MailServiceImpl(MailContentBuilder mailContentBuilder, JavaMailSender javaMailSender) {
		this.mailContentBuilder = mailContentBuilder;
		this.javaMailSender = javaMailSender;

	}

	@Override
	public void sendResetPasswordMail(User user) {
		try {
			MimeMessage mail = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);

			helper.setTo(user.getEmail());
			String title = String.format(titleResetPassword, user.toString());
			helper.setSubject(title);
			String content = mailContentBuilder
				.build( user, "mail/mailResetPassword");
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

	@Override
	public void sendNotificationByMail(User user, Comment comment) {
		try {
			MimeMessage mail = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);

			helper.setTo(user.getEmail());
			helper.setSubject(titleNotification);
			String content = mailContentBuilder.build(user, "mail/mailNotification", comment);
			helper.setText(content, true);
			try {
				javaMailSender.send(mail);
			} catch (Exception e) {
				logger.error("This error appeared because the mail was not sent");
			}

		} catch (Exception e) {
			logger.error("This error appeared because the mail notification was not sent");
		}
	}
}

