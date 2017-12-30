package main.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.text.SimpleDateFormat;
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

	@JoinColumn(name = "created_by", referencedColumnName = "id")
	@ManyToOne
	private User createdBy;

	@Column(name = "send_to")
	private String sentTo;

	@Column(name = "content")
	private String content;

	@Column(name = "deleted")
	private String deleted;

	@Column(name = "time")
	private Date time;

	@ManyToOne
	private Comment parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
	private List<Comment> answers;

	public Comment() {
	}

	public Comment(String content, User createdBy, String sentTo, Date time) {
		this.content = content;
		this.createdBy = createdBy;
		this.sentTo = sentTo;
		this.time = time;
	}

	public Comment(String content, User createdBy, String sentTo, Date time, Comment parent) {
		this(content, createdBy, sentTo, time);
		this.parent = parent;
	}

	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy H:mm");
		String result = formatter.format(time);
		return result;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Long getId() {
		return id;
	}

	public Comment getParent() {
		return parent;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<Comment> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Comment> answers) {
		this.answers = answers;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public String getSentTo() {
		return sentTo;
	}

	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Comment comment = (Comment) o;

		if (!id.equals(comment.id)) return false;
		if (createdBy != null ? !createdBy.equals(comment.createdBy) : comment.createdBy != null)
			return false;
		if (sentTo != null ? !sentTo.equals(comment.sentTo) : comment.sentTo != null) return false;
		if (content != null ? !content.equals(comment.content) : comment.content != null)
			return false;
		if (deleted != null ? !deleted.equals(comment.deleted) : comment.deleted != null)
			return false;
		if (time != null ? !time.equals(comment.time) : comment.time != null) return false;
		return answers != null ? answers.equals(comment.answers) : comment.answers == null;
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
		result = 31 * result + (sentTo != null ? sentTo.hashCode() : 0);
		result = 31 * result + (content != null ? content.hashCode() : 0);
		result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
		result = 31 * result + (time != null ? time.hashCode() : 0);
		result = 31 * result + (answers != null ? answers.hashCode() : 0);
		return result;
	}

}
