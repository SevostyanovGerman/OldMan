package main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@JsonBackReference
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "sec_name")
	private String secName;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "phone", nullable = false)
	private String phone;

	@Column(name = "creation_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@JsonIgnore
	@OneToMany(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "delievery_id")
	private List<Delivery> deliveries;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	private List<Order> orders;

	@Column(name = "deleted")
	private boolean deleted;

	public Customer() {
		this.deliveries = new ArrayList<>();
	}

	public Customer(String firstName, String secName, String email, String phone, Delivery deliveries, String country,
					String city, String address, String zip) {
		this.firstName = firstName;
		this.secName = secName;
		this.email = email;
		this.phone = phone;
		this.creationDate = new Date();
		if (this.deliveries == null) {
			List<Delivery> deliveryList = new ArrayList<>();
			this.deliveries = deliveryList;
			this.deliveries.add(deliveries);
		} else {
			this.deliveries.add(deliveries);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecName() {
		return secName;
	}

	public void setSecName(String secName) {
		this.secName = secName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Order> getOrders() {
		return this.orders;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return firstName + " " + secName;
	}

	public Delivery getDefaultDelivery() {
		if (this.deliveries != null) {
			return this.getDeliveries().get(0);
		}
		return null;
	}

	public void updateCustomerFields(String firstName, String secName, String email, String phone) {
		this.firstName = firstName;
		this.secName = secName;
		this.email = email;
		this.phone = phone;
	}

	public boolean getDeleted() {
		return this.deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Customer customer = (Customer) o;
		if (!Objects.equals(this.id, customer.id)) return false;
		if (!Objects.equals(this.firstName, customer.firstName)) return false;
		if (!Objects.equals(this.secName, customer.secName)) return false;
		if (!Objects.equals(this.email, customer.email)) return false;
		if (!Objects.equals(this.phone, customer.phone)) return false;
		if (!Objects.equals(this.creationDate, customer.creationDate)) return false;
		if (!Objects.equals(this.deliveries, customer.deliveries)) return false;
		if (!Objects.equals(this.deleted, customer.deleted)) return false;
		return Objects.equals(this.orders, customer.orders);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(this.id);
		result = 42 * result + (Objects.hashCode(this.firstName));
		result = 42 * result + (Objects.hashCode(this.secName));
		result = 42 * result + (Objects.hashCode(this.email));
		result = 42 * result + (Objects.hashCode(this.phone));
		result = 42 * result + (Objects.hashCode(this.creationDate));
		result = 42 * result + (Objects.hashCode(this.deliveries));
		result = 42 * result + (Objects.hashCode(this.orders));
		result = 42 * result + (Objects.hashCode(this.deleted));
		return result;
	}
}

