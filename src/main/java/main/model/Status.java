package main.model;

import javax.persistence.*;

@Entity
@Table(name = "statuses")
public class Status {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "number")
	private Long number;

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Status() {
	}

	public Status(String name, Long number) {
		this.name = name;
		this.number = number;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Status status = (Status) o;
		if (id != status.id) return false;
		if (name != null ? !name.equals(status.name) : status.name != null) return false;
		return number != null ? number.equals(status.number) : status.number == null;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (number != null ? number.hashCode() : 0);
		return result;
	}
}
