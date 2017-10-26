package main.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {

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

	@Column(name = "Time")
	private Date time;

	@OneToMany(fetch = FetchType.EAGER, targetEntity = Answer.class)
	@JoinTable(name = "keys_comment_answer", joinColumns = {@JoinColumn(name = "comment_id")},
		inverseJoinColumns = {@JoinColumn(name = "answer_id")})
	private List <Answer> answers;

	public Comment() {
	}

	public Comment(String content, String login) {
		this.content = content;
		this.login = login;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

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

	public List <Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List <Answer> answers) {
		this.answers = answers;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Comment comment = (Comment) o;
		if (id != null ? !id.equals(comment.id) : comment.id != null) return false;
		if (login != null ? !login.equals(comment.login) : comment.login != null) return false;
		return content != null ? content.equals(comment.content) : comment.content == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (login != null ? login.hashCode() : 0);
		result = 31 * result + (content != null ? content.hashCode() : 0);
		return result;
	}
}
