package main.model;

import javax.persistence.*;

@Entity
@Table(name = "notification")
public class Notification {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "recipient", nullable = false)
	private String recipient;

	@Column(name = "is_read")
	private boolean isRead;

	@Column(name = "order_id")
	private Long orderId;

	public Notification() {
	}

	public Notification(String recipient,
						Long order) {
		this.recipient = recipient;
		this.orderId = order;
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

}
