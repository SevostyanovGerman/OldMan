package main.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
public class Order implements Comparable<Order>, Comparator<Order> {

	private static final DateTimeFormatter DATE_TIME_FORMATTER =
		DateTimeFormat.forPattern("dd MMMM, yyyy");

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

	@OneToOne
	@JoinColumn(name = "deliveryType_id")
	private DeliveryType deliveryType;

	@Column(name = "date_recieved")
	private Date dateRecieved;

	@Column(name = "date_transferred")
	private Date dateTransferred;

	@Column(name = "o_from")
	private String from;

	@Column(name = "o_to")
	private String to;

	@ManyToOne
	@JoinColumn(name = "delievery_id")
	@JsonBackReference
	private Delivery delivery;

	@ManyToOne
	@JoinColumn(name = "paymentType_id")
	@JsonBackReference
	private Payment paymentType;

	@ManyToOne
	@JoinColumn(name = "status_id")
	@JsonBackReference
	private Status status;

	@OneToMany
	@JoinColumn(name = "order_id")
	@JsonBackReference
	private List<Comment> comments;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private List<Item> items;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	@JsonBackReference
	private Customer customer;

	@OneToMany
	@JoinColumn(name = "history_id")
	@JsonBackReference
	private List<History> histories;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	@JsonBackReference
	private User manager;

	@ManyToOne
	@JoinColumn(name = "master_id")
	@JsonBackReference
	private User master;

	@ManyToOne
	@JoinColumn(name = "designer_id")
	@JsonBackReference
	private User designer;

	public Order() {
		this.created = new Date();
	}

	//Constructor for new Order
	public Order(Boolean payment, Boolean deleted, Date created, Status status, User manager) {
		this.payment = payment;
		this.deleted = deleted;
		this.created = created;
		this.status = status;
		this.manager = manager;
	}

	public Order(String number, Boolean payment, Boolean deleted, Date created,
				 DeliveryType deliveryType, Payment paymentType, Status status, Customer customer,
				 Item item, User manager, User designer, User master) {
		this.number = number;
		this.payment = payment;
		this.deleted = deleted;
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
		addItem(item);
		this.delivery = customer.getDefaultDelivery();
		this.price = item.getAmount();
	}

	public Order(String number, Boolean payment, Boolean deleted, Date created,
				 DeliveryType deliveryType, Payment paymentType, Status status, Customer customer,
				 Item item, User manager, User designer, User master, Delivery delivery) {
		this.number = number;
		this.payment = payment;
		this.deleted = deleted;
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
		addItem(item);
		this.delivery = delivery;
		this.price = item.getAmount();
	}

	public void addItem(Item item) {
		if (this.items == null) {
			this.items = new ArrayList<>();
		}
		items.add(item);
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
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Boolean getPayment() {
		return payment;
	}

	public String getPaymentString() {
		if (payment == null) {
			return "не оплачен";
		}
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

	@Access(AccessType.PROPERTY)
	public String getPaymentTypeString() {
		if (paymentType == null) {
			return "не выбран";
		}
		return paymentType.getName();
	}

	@Access(AccessType.PROPERTY)
	public String getCustomerFirstNameString() {
		if (customer == null) {
			return "";
		}
		return customer.getFirstName();
	}

	@Access(AccessType.PROPERTY)
	public void setCustomerFirstNameString(String firstName) {

	}

	@Access(AccessType.PROPERTY)
	public String getCustomerSecNameString() {
		if (customer == null) {
			return "";
		}
		return customer.getSecName();
	}

	@Access(AccessType.PROPERTY)
	public void setCustomerSecNameString(String secName) {
	}

	@Access(AccessType.PROPERTY)
	public String getMasterFirstNameString() {
		if (master == null) {
			return "";
		}
		return master.getFirstName();
	}

	@Access(AccessType.PROPERTY)
	public void setMasterFirstNameString(String name) {

	}

	@Access(AccessType.PROPERTY)
	public String getMasterSecNameString() {
		if (master == null) {
			return "";
		}
		return master.getSecName();
	}

	@Access(AccessType.PROPERTY)
	public void setMasterSecNameString(String name) {
	}

	@Access(AccessType.PROPERTY)
	public String getDesignerFirstNameString() {
		if (designer == null) {
			return "";
		}
		return designer.getFirstName();
	}

	@Access(AccessType.PROPERTY)
	public void setDesignerFirstNameString(String name) {
	}

	@Access(AccessType.PROPERTY)
	public String getDesignerSecNameString() {
		if (designer == null) {
			return "";
		}
		return designer.getSecName();
	}

	@Access(AccessType.PROPERTY)
	public void setDesignerSecNameString(String name) {
	}

	public void setPaymentTypeString(String paymentTypeString) {

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

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}

	public List<Comment> getComments() {
		return comments.stream().filter(comment -> comment.getParent() == null)
			.collect(Collectors.toList());
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Item> getItems() {
		return items;
	}

	@Deprecated
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
		setCustomerFirstNameString(customer.getFirstName());
		setCustomerSecNameString(customer.getSecName());
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

	public double getAmount() {
		double amount = 0;
		for (int i = 0; i < this.items.size(); i++) {
			amount += this.items.get(i).getAmount();
		}
		return amount;
	}

	public void deductPrice(double price) {
		this.price -= price;
	}

	public void addPrice(double price) {
		this.price += price;
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Order order = (Order) o;
		if (Double.compare(order.price, price) != 0) {
			return false;
		}
		if (id != null ? !id.equals(order.id) : order.id != null) {
			return false;
		}
		if (number != null ? !number.equals(order.number) : order.number != null) {
			return false;
		}
		if (payment != null ? !payment.equals(order.payment) : order.payment != null) {
			return false;
		}
		if (deleted != null ? !deleted.equals(order.deleted) : order.deleted != null) {
			return false;
		}
		if (created != null ? !created.equals(order.created) : order.created != null) {
			return false;
		}
		if (deliveryType != null ? !deliveryType.equals(order.deliveryType) :
			order.deliveryType != null) {
			return false;
		}
		if (dateRecieved != null ? !dateRecieved.equals(order.dateRecieved) :
			order.dateRecieved != null) {
			return false;
		}
		if (dateTransferred != null ? !dateTransferred.equals(order.dateTransferred) :
			order.dateTransferred != null) {
			return false;
		}
		if (from != null ? !from.equals(order.from) : order.from != null) {
			return false;
		}
		if (to != null ? !to.equals(order.to) : order.to != null) {
			return false;
		}
		if (delivery != null ? !delivery.equals(order.delivery) : order.delivery != null) {
			return false;
		}
		if (paymentType != null ? !paymentType.equals(order.paymentType) :
			order.paymentType != null) {
			return false;
		}
		if (status != null ? !status.equals(order.status) : order.status != null) {
			return false;
		}
		if (comments != null ? !comments.equals(order.comments) : order.comments != null) {
			return false;
		}
		if (items != null ? !items.equals(order.items) : order.items != null) {
			return false;
		}
		if (customer != null ? !customer.equals(order.customer) : order.customer != null) {
			return false;
		}
		if (histories != null ? !histories.equals(order.histories) : order.histories != null) {
			return false;
		}
		if (manager != null ? !manager.equals(order.manager) : order.manager != null) {
			return false;
		}
		if (master != null ? !master.equals(order.master) : order.master != null) {
			return false;
		}
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

	@Override
	public int compareTo(Order o) {
//		return this.number.compareTo(o.number);
		return this.customer.getFirstName().compareTo(o.customer.getFirstName());

	}

	@Override
	public int compare(Order a, Order b) {

		return Integer.parseInt(a.getNumber()) - Integer.parseInt(b.getNumber());
	}

	public static Comparator<Order> nameComparator = new Comparator<Order>() {

		@Override
		public int compare(Order a, Order b) {
			return a.getCustomer().getFirstName().compareTo(b.getCustomer().getFirstName());
		}
	};

	public static Comparator<Order> priceComparator = new Comparator<Order>() {

		@Override
		public int compare(Order a, Order b) {
			return (int) a.getPrice() - (int) b.getPrice();
		}
	};

	public static Comparator<Order> numberComparator = new Comparator<Order>() {

		@Override
		public int compare(Order a, Order b) {
			return Integer.parseInt(a.getNumber()) - Integer.parseInt(b.getNumber());
		}
	};

	public static Comparator<Order> statusComparator = new Comparator<Order>() {

		@Override
		public int compare(Order a, Order b) {
			return (int) a.getStatus().getNumber() - (int) b.getStatus().getNumber();
		}
	};
}
