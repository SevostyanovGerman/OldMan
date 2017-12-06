package main.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonBackReference
	private Long id;

	@Column(name = "login")
	private String login;

	@Column(name = "content")
	private String content;

	@Column(name = "deleted")
	private Boolean deleted;

	@Column(name = "time")
	private Date time;

	@OneToMany(fetch = FetchType.EAGER, targetEntity = Comment.class)
	@JoinTable(name = "keys_comment_answer", joinColumns = {@JoinColumn(name = "comment_id")},
			   inverseJoinColumns = {@JoinColumn(name = "answer_id")})
	private List<Comment> answers;

	public Comment() {
	}

	public Comment(String content, String login, Date time) {
		this.content = content;
		this.login = login;
		this.time = time;
		this.deleted = false;
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

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public List<Comment> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Comment> answers) {
		this.answers = answers;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Comment comment = (Comment) o;
		if (id != null ? !id.equals(comment.id) : comment.id != null) {
			return false;
		}
		if (login != null ? !login.equals(comment.login) : comment.login != null) {
			return false;
		}
		if (content != null ? !content.equals(comment.content) : comment.content != null) {
			return false;
		}
		if (deleted != null ? !deleted.equals(comment.deleted) : comment.deleted != null) {
			return false;
		}
		if (time != null ? !time.equals(comment.time) : comment.time != null) {
			return false;
		}
		return answers != null ? answers.equals(comment.answers) : comment.answers == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (login != null ? login.hashCode() : 0);
		result = 31 * result + (content != null ? content.hashCode() : 0);
		result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
		result = 31 * result + (time != null ? time.hashCode() : 0);
		result = 31 * result + (answers != null ? answers.hashCode() : 0);
		return result;
	}
}
