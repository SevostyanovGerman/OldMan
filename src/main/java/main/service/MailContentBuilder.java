package main.service;

import main.model.Comment;
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

	protected String build(User user, String template, String link) {
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("link", link);
		return templateEngine.process(template, context);
	}

	protected String build(User user, String template, Comment comment, String link) {
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("comment", comment);
		context.setVariable("link", link);
		return templateEngine.process(template, context);
	}
}
