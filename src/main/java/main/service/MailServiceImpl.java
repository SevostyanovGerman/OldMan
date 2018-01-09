package main.service;

import javax.mail.internet.MimeMessage;
import main.model.Mail;
import main.model.Mail.MailNames;
import main.repository.MailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

	private MailContentBuilder mailContentBuilder;
	private JavaMailSender javaMailSender;
	private MailRepository mailRepository;

	private final static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Autowired
	public MailServiceImpl(MailContentBuilder mailContentBuilder, JavaMailSender javaMailSender,
		MailRepository mailRepository) {
		this.mailContentBuilder = mailContentBuilder;
		this.javaMailSender = javaMailSender;
		this.mailRepository = mailRepository;
	}

	@Override
	public void sendEmail(Mail newMail) {
		try {
			MimeMessage mail = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);

			helper.setTo(newMail.getForUser().getEmail());
			helper.setSubject(newMail.getTitle());
			String content = mailContentBuilder
				.build(newMail.getMessage(), newMail.getForUser(), newMail.getTemplate());
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

	public Mail getByMailName(MailNames nameMail) {
		return mailRepository.getByMailName(nameMail.toString());
	}

	@Override
	public void save(Mail mail) {
		mailRepository.save(mail);
	}

}

