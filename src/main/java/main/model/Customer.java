package main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "sec_name")
	private String secName;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "phone", unique = true, nullable = false)
	private String phone;

	@Column(name = "creation_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "keys_customer_delivery", joinColumns = {@JoinColumn(name = "customer_id")},
		inverseJoinColumns = {@JoinColumn(name = "delivery_id")})
	private List <Delivery> deliveries;

	@JsonIgnore
	@OneToMany
	@JoinTable(name = "keys_order_customer", joinColumns = {@JoinColumn(name = "customer_id")},
		inverseJoinColumns = {@JoinColumn(name = "order_id")})
	private List <Order> orders;

	public Customer() {
	}

	public Customer(String firstName, String secName, String email, String phone, Delivery deliveries) {
		this.firstName = firstName;
		this.secName = secName;
		this.email = email;
		this.phone = phone;
		if (this.deliveries == null) {
			List <Delivery> deliveryList = new ArrayList <>();
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

	public List <Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List <Delivery> deliveries) {
		this.deliveries = deliveries;
	}

	public void setOrders(List <Order> orders) {
		this.orders = orders;
	}

	public List <Order> getOrders() {
		return this.orders;
	}

	@Override
	public String toString() {
		return firstName + " " + secName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Customer customer = (Customer) o;
		if (id != null ? !id.equals(customer.id) : customer.id != null) return false;
		if (firstName != null ? !firstName.equals(customer.firstName) : customer.firstName != null) return false;
		if (secName != null ? !secName.equals(customer.secName) : customer.secName != null) return false;
		if (email != null ? !email.equals(customer.email) : customer.email != null) return false;
		if (phone != null ? !phone.equals(customer.phone) : customer.phone != null) return false;
		return deliveries != null ? deliveries.equals(customer.deliveries) : customer.deliveries == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (secName != null ? secName.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (deliveries != null ? deliveries.hashCode() : 0);
		return result;
	}
}
