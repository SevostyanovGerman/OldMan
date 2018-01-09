package main.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import main.constans.RegexpConstans;

@Entity
@Table(name = "statuses")
public class Status {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Pattern(regexp = RegexpConstans.REG_EXP_GENERAL, message = "{general.wrong.message}")
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "number")
	private long number;

	@Column(name = "deleted")
	private boolean deleted;

	@Pattern(regexp = RegexpConstans.REG_EXP_COLOR, message = "{formatcolor.wrong}")
	@Column(name = "color")
	private String color;

	public void setNumber(Long number) {
		this.number = number;
	}

	public Status() {
	}

	public Status(String name, Long number, String color) {
		this.name = name;
		this.number = number;
		this.color = color;
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

	public boolean getDeleted() {
		return this.deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public long getNumber() {
		return this.number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (getClass() != o.getClass())) {
			return false;
		}
		Status status = (Status) o;
		if (!Objects.equals(this.id, status.id)) {
			return false;
		}
		if (!Objects.equals(this.name, status.name)) {
			return false;
		}
		if (!Objects.equals(this.number, status.number)) {
			return false;
		}
		if (!Objects.equals(this.color, status.color)) {
			return false;
		}
		return Objects.equals(this.deleted, status.deleted);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 42 * result + Objects.hashCode(this.name);
		result = 42 * result + Objects.hashCode(this.number);
		result = 42 * result + Objects.hashCode(this.color);
		result = 42 * result + Objects.hashCode(this.deleted);
		return result;
	}
}
