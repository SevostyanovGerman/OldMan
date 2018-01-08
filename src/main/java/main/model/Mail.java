package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mail")
public class Mail {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "mailName")
	private String mailName;

	@Column(name = "title")
	private String title;

	@Column(name = "forUser")
	private User forUser;

	@Column(name = "message")
	private String message;

	@Column(name = "template")
	private String template;

	public Mail() {
	}

	public Mail(String mailName, String title, User forUser, String message, String template) {
		this.mailName = mailName;
		this.title = title;
		this.forUser = forUser;
		this.message = message;
		this.template = template;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getForUser() {
		return forUser;
	}

	public void setForUser(User forUser) {
		this.forUser = forUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMailName() {
		return mailName;
	}

	public void setMailName(String mailName) {
		this.mailName = mailName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Mail mail = (Mail) o;

		if (id != null ? !id.equals(mail.id) : mail.id != null) {
			return false;
		}
		if (mailName != null ? !mailName.equals(mail.mailName) : mail.mailName != null) {
			return false;
		}
		if (title != null ? !title.equals(mail.title) : mail.title != null) {
			return false;
		}
		if (forUser != null ? !forUser.equals(mail.forUser) : mail.forUser != null) {
			return false;
		}
		if (message != null ? !message.equals(mail.message) : mail.message != null) {
			return false;
		}
		return template != null ? template.equals(mail.template) : mail.template == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (mailName != null ? mailName.hashCode() : 0);
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (forUser != null ? forUser.hashCode() : 0);
		result = 31 * result + (message != null ? message.hashCode() : 0);
		result = 31 * result + (template != null ? template.hashCode() : 0);
		return result;
	}

	public enum MailNames {
		RESET_PASSWORD,
		NOTIFICATION
	}
}
