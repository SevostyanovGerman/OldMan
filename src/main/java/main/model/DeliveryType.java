package main.model;

import javax.persistence.*;

@Entity
@Table(name = "deliveryType")
public class DeliveryType {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "pickup")
	private Boolean pickup;

	public DeliveryType() {
	}

	public DeliveryType(String name, Boolean pickup) {
		this.name = name;
		this.pickup = pickup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		DeliveryType that = (DeliveryType) o;
		if (id != null ? !id.equals(that.id) : that.id != null) {
			return false;
		}
		if (name != null ? !name.equals(that.name) : that.name != null) {
			return false;
		}
		return pickup != null ? pickup.equals(that.pickup) : that.pickup == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (pickup != null ? pickup.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
