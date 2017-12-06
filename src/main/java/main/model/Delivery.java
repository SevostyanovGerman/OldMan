package main.model;

import javax.persistence.*;

@Entity
@Table(name = "delivery")
public class Delivery {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "country")
	private String country;

	@Column(name = "city")
	private String city;

	@Column(name = "address")
	private String address;

	@Column(name = "zip")
	private String zip;

	@Column(name = "pickup")
	private Boolean pickup;

	public Delivery() {
		this.pickup = false;
	}

	public Delivery(String country, String city, String address, String zip) {
		this.country = country;
		this.city = city;
		this.address = address;
		this.zip = zip;
		this.pickup = false;
	}

	public Delivery(String country, String city, String address, String zip, boolean pickup) {
		this.country = country;
		this.city = city;
		this.address = address;
		this.zip = zip;
		this.pickup = pickup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("");
		builder.append(this.country);
		builder.append(" , ");
		builder.append(this.getCity());
		builder.append(" , ");
		builder.append(this.getAddress());
		builder.append(" , ");
		builder.append(this.zip);
		return builder.toString();
	}

	public Boolean getPickup() {
		return pickup;
	}

	public void setPickup(Boolean pickup) {
		this.pickup = pickup;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Delivery delivery = (Delivery) o;
		if (country != null ? !country.equals(delivery.country) : delivery.country != null) {
			return false;
		}
		if (city != null ? !city.equals(delivery.city) : delivery.city != null) {
			return false;
		}
		if (address != null ? !address.equals(delivery.address) : delivery.address != null) {
			return false;
		}
		if (zip != null ? !zip.equals(delivery.zip) : delivery.zip != null) {
			return false;
		}
		return pickup != null ? pickup.equals(delivery.pickup) : delivery.pickup == null;
	}

	@Override
	public int hashCode() {
		int result = country != null ? country.hashCode() : 0;
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (zip != null ? zip.hashCode() : 0);
		result = 31 * result + (pickup != null ? pickup.hashCode() : 0);
		return result;
	}
}
