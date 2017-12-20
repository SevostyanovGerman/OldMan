package main.service;

import main.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {

	private TemplateEngine templateEngine;

	@Autowired
	public MailContentBuilder(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public String build(String message, User user, String template) {
		Context context = new Context();
		context.setVariable("message", message);
		context.setVariable("user", user);

		return templateEngine.process(template, context);
	}
}
