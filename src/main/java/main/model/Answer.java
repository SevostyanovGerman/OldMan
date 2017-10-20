package main.model;

import javax.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "login")
	private String login;

	@Column(name = "content")
	private String content;

	@Column(name = "deleted")
	private String deleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public Answer(){

	}

	public Answer(String content, String login) {
		this.content = content;
		this.login = login;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Answer answer = (Answer) o;

		if (id != null ? !id.equals(answer.id) : answer.id != null) return false;
		if (login != null ? !login.equals(answer.login) : answer.login != null) return false;
		if (content != null ? !content.equals(answer.content) : answer.content != null) return false;
		return deleted != null ? deleted.equals(answer.deleted) : answer.deleted == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (login != null ? login.hashCode() : 0);
		result = 31 * result + (content != null ? content.hashCode() : 0);
		result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
		return result;
	}
}
