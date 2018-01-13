package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import main.constans.RegexpConstans;

@Entity
@Table(name = "notification")
public class Notification {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Pattern(regexp = RegexpConstans.REG_EXP_OF_LOGIN, message = "{user.login.wrong}")
	@Column(name = "recipient", nullable = false)
	private String recipient;

	@Column(name = "is_read")
	private boolean isRead;

	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "comment_id")
	private Long commentId;

	public Notification() {
	}

	public Notification(String recipient, Long order, Long commentId) {
		this.recipient = recipient;
		this.orderId = order;
		this.commentId = commentId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecepient() {
		return recipient;
	}

	public void setRecepient(String recepient) {
		this.recipient = recepient;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public Long getOrder() {
		return orderId;
	}

	public void setOrder(Long order) {
		this.orderId = orderId;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
}
