package main.model;

public class Mail {

	private String mailName;
	private String title;
	private User forUser;
	private String message;
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

	public void setTitleParametr(String parametr) {
		this.title = String.format(this.title, parametr);
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

	public void setMessageParametr(String parametr) {
		this.message = String.format(this.title, parametr);
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
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
		int result = mailName != null ? mailName.hashCode() : 0;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (forUser != null ? forUser.hashCode() : 0);
		result = 31 * result + (message != null ? message.hashCode() : 0);
		result = 31 * result + (template != null ? template.hashCode() : 0);
		return result;
	}

}
