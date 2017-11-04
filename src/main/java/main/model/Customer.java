package main.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
	@JsonBackReference
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

	@Column(name = "country")
	private String country;

	@Column(name = "city")
	private String city;

	@Column(name = "address")
	private String address;

	@Column(name = "zip")
	private String zip;

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
		this.deliveries = new ArrayList <>();
	}

	public Customer(String firstName, String secName, String email, String phone, Delivery deliveries, String country,
					String city, String address, String zip) {
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
		this.country = country;
		this.city = city;
		this.address = address;
		this.zip = zip;
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

	public String getDefaultDelivery() {
		StringBuilder builder = new StringBuilder("");
		builder.append(this.country);
		builder.append(" , ");
		builder.append(this.city);
		builder.append(" , ");
		builder.append(this.address);
		builder.append(" , ");
		builder.append(this.zip);
		return builder.toString();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public void updateAddressFields(String country, String city, String address, String zip) {
		this.country = country;
		this.city = city;
		this.address = address;
		this.zip = zip;
	}

	public void updateCustomerFields(String firstName, String secName, String email, String phone) {
		this.firstName = firstName;
		this.secName = secName;
		this.email = email;
		this.phone = phone;
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
		if (creationDate != null ? !creationDate.equals(customer.creationDate) : customer.creationDate != null)
			return false;
		if (country != null ? !country.equals(customer.country) : customer.country != null) return false;
		if (city != null ? !city.equals(customer.city) : customer.city != null) return false;
		if (address != null ? !address.equals(customer.address) : customer.address != null) return false;
		if (zip != null ? !zip.equals(customer.zip) : customer.zip != null) return false;
		if (deliveries != null ? !deliveries.equals(customer.deliveries) : customer.deliveries != null) return false;
		return orders != null ? orders.equals(customer.orders) : customer.orders == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (secName != null ? secName.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
		result = 31 * result + (country != null ? country.hashCode() : 0);
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (zip != null ? zip.hashCode() : 0);
		result = 31 * result + (deliveries != null ? deliveries.hashCode() : 0);
		result = 31 * result + (orders != null ? orders.hashCode() : 0);
		return result;
	}
}
