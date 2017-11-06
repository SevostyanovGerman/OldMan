package main.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd MMMM, yyyy");

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "number")
	private String number;

	@Column(name = "price")
	private double price;

	@Column(name = "payment")
	private Boolean payment;

	@Column(name = "deleted")
	private Boolean deleted;

	@Column(name = "created")
	private Date created;

	@Column(name = "delivery_type")
	private String deliveryType;

	@Column(name = "date_recieved")
	private Date dateRecieved;

	@Column(name = "date_transferred")
	private Date dateTransferred;

	@Column(name = "o_from")
	private String from;

	@Column(name = "o_to")
	private String to;

	@OneToOne(fetch = FetchType.EAGER, targetEntity = Delivery.class)
	@JoinTable(name = "keys_order_delivery", joinColumns = {@JoinColumn(name = "order_id")},
		inverseJoinColumns = {@JoinColumn(name = "delivery_id")})
	@JsonBackReference
	private Delivery delivery;

	@OneToOne(fetch = FetchType.EAGER, targetEntity = Payment.class)
	@JoinTable(name = "keys_order_payment", joinColumns = {@JoinColumn(name = "order_id")},
		inverseJoinColumns = {@JoinColumn(name = "payment_id")})
	@JsonBackReference
	private Payment paymentType;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Status.class)
	@JoinTable(name = "keys_order_status", joinColumns = {@JoinColumn(name = "order_id")},
		inverseJoinColumns = {@JoinColumn(name = "status_id")})
	@JsonBackReference
	private Status status;

	@OneToMany(fetch = FetchType.EAGER, targetEntity = Comment.class)
	@JoinTable(name = "keys_order_comment", joinColumns = {@JoinColumn(name = "order_id")},
		inverseJoinColumns = {@JoinColumn(name = "comment_id")})
	@JsonBackReference
	private List<Comment> comments;

	@OneToMany(fetch = FetchType.LAZY, targetEntity = Item.class)
	@JoinTable(name = "keys_order_item", joinColumns = {@JoinColumn(name = "order_id")},
		inverseJoinColumns = {@JoinColumn(name = "item_id")})
	@JsonBackReference
	private List<Item> items;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinTable(name = "keys_order_customer", joinColumns = {@JoinColumn(name = "order_id")},
		inverseJoinColumns = {@JoinColumn(name = "customer_id")})
	@JsonBackReference
	private Customer customer;

	@OneToMany(fetch = FetchType.LAZY, targetEntity = History.class)
	@JoinTable(name = "keys_order_history", joinColumns = {@JoinColumn(name = "order_id")},
		inverseJoinColumns = {@JoinColumn(name = "history_id")})
	@JsonBackReference
	private List<History> histories;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinTable(name = "keys_order_manager", joinColumns = {@JoinColumn(name = "order_id")},
		inverseJoinColumns = {@JoinColumn(name = "manager_id")})
	@JsonBackReference
	private User manager;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinTable(name = "keys_order_master", joinColumns = {@JoinColumn(name = "order_id")},
		inverseJoinColumns = {@JoinColumn(name = "master_id")})
	@JsonBackReference
	private User master;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinTable(name = "keys_order_designer", joinColumns = {@JoinColumn(name = "order_id")},
		inverseJoinColumns = {@JoinColumn(name = "designer_id")})
	@JsonBackReference
	private User designer;

	public Order() {
	}

	//Constructor for new Order
	public Order(Boolean payment, Boolean deleted, Date created, Status status, User manager) {
		this.payment = payment;
		this.deleted = deleted;
		this.created = created;
		this.status = status;
		this.manager = manager;
	}

	public Order(String number, Boolean payment, Boolean deleted, double price, Date created, String deliveryType,
				 Payment paymentType, Status status, Customer customer, Item item, User manager, User designer,
				 User master) {
		this.number = number;
		this.payment = payment;
		this.deleted = deleted;
		this.price = price;
		this.created = created;
		this.deliveryType = deliveryType;
		this.paymentType = paymentType;
		this.status = status;
		this.customer = customer;
		this.manager = manager;
		this.designer = designer;
		this.master = master;
		if (this.items == null) {
			this.items = new ArrayList<>();
		}
		this.items.add(item);
		this.delivery = customer.getDefaultDelivery();
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Boolean getPayment() {
		return payment;
	}

	public String getPaymentString() {
		if (payment) {
			return "оплачен";
		}
		return "не оплачен";
	}

	public void setPayment(Boolean payment) {
		this.payment = payment;
	}

	public Payment getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Payment paymentType) {
		this.paymentType = paymentType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public User getDesigner() {
		return designer;
	}

	public void setDesigner(User designer) {
		this.designer = designer;
	}

	public User getMaster() {
		return master;
	}

	public void setMaster(User master) {
		this.master = master;
	}

	public String getDateRecieved() {
		DateTime dateTime = new DateTime(dateRecieved);
		return dateTime.toString(DATE_TIME_FORMATTER);
	}

	public Date getDateRecievedDate() {
		return dateRecieved;
	}

	public void setDateRecieved(Date dateRecieved) {
		this.dateRecieved = dateRecieved;
	}

	public String getDateTransferred() {
		DateTime dateTime = new DateTime(dateTransferred);
		return dateTime.toString(DATE_TIME_FORMATTER);
	}

	public Date getDateTransferredDate() {
		return dateTransferred;
	}

	public void setDateTransferred(Date dateTransferred) {
		this.dateTransferred = dateTransferred;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCreated() {
		DateTime dateTime = new DateTime(created);
		return dateTime.toString(DATE_TIME_FORMATTER);
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<History> getHistories() {
		return histories;
	}

	public void setHistories(List<History> histories) {
		this.histories = histories;
	}

	public String getDefaultDelivery() {
		if (this.delivery != null) {
			StringBuilder builder = new StringBuilder("");
			builder.append(this.delivery.getCountry());
			builder.append(" , ");
			builder.append(this.delivery.getCity());
			builder.append(" , ");
			builder.append(this.delivery.getAddress());
			builder.append(" , ");
			builder.append(this.delivery.getZip());
			return builder.toString();
		}
		return "--";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		if (Double.compare(order.price, price) != 0) return false;
		if (id != null ? !id.equals(order.id) : order.id != null) return false;
		if (number != null ? !number.equals(order.number) : order.number != null) return false;
		if (payment != null ? !payment.equals(order.payment) : order.payment != null) return false;
		if (deleted != null ? !deleted.equals(order.deleted) : order.deleted != null) return false;
		if (created != null ? !created.equals(order.created) : order.created != null) return false;
		if (deliveryType != null ? !deliveryType.equals(order.deliveryType) : order.deliveryType != null) return false;
		if (dateRecieved != null ? !dateRecieved.equals(order.dateRecieved) : order.dateRecieved != null) return false;
		if (dateTransferred != null ? !dateTransferred.equals(order.dateTransferred) : order.dateTransferred != null)
			return false;
		if (from != null ? !from.equals(order.from) : order.from != null) return false;
		if (to != null ? !to.equals(order.to) : order.to != null) return false;
		if (delivery != null ? !delivery.equals(order.delivery) : order.delivery != null) return false;
		if (paymentType != null ? !paymentType.equals(order.paymentType) : order.paymentType != null) return false;
		if (status != null ? !status.equals(order.status) : order.status != null) return false;
		if (comments != null ? !comments.equals(order.comments) : order.comments != null) return false;
		if (items != null ? !items.equals(order.items) : order.items != null) return false;
		if (customer != null ? !customer.equals(order.customer) : order.customer != null) return false;
		if (histories != null ? !histories.equals(order.histories) : order.histories != null) return false;
		if (manager != null ? !manager.equals(order.manager) : order.manager != null) return false;
		if (master != null ? !master.equals(order.master) : order.master != null) return false;
		return designer != null ? designer.equals(order.designer) : order.designer == null;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = id != null ? id.hashCode() : 0;
		result = 31 * result + (number != null ? number.hashCode() : 0);
		temp = Double.doubleToLongBits(price);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (payment != null ? payment.hashCode() : 0);
		result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
		result = 31 * result + (created != null ? created.hashCode() : 0);
		result = 31 * result + (deliveryType != null ? deliveryType.hashCode() : 0);
		result = 31 * result + (dateRecieved != null ? dateRecieved.hashCode() : 0);
		result = 31 * result + (dateTransferred != null ? dateTransferred.hashCode() : 0);
		result = 31 * result + (from != null ? from.hashCode() : 0);
		result = 31 * result + (to != null ? to.hashCode() : 0);
		result = 31 * result + (delivery != null ? delivery.hashCode() : 0);
		result = 31 * result + (paymentType != null ? paymentType.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (comments != null ? comments.hashCode() : 0);
		result = 31 * result + (items != null ? items.hashCode() : 0);
		result = 31 * result + (customer != null ? customer.hashCode() : 0);
		result = 31 * result + (histories != null ? histories.hashCode() : 0);
		result = 31 * result + (manager != null ? manager.hashCode() : 0);
		result = 31 * result + (master != null ? master.hashCode() : 0);
		result = 31 * result + (designer != null ? designer.hashCode() : 0);
		return result;
	}
}
