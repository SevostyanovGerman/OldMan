package main.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "statuses")
public class Status {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "number")
	private long number;

	@Column(name = "deleted")
	private boolean deleted;

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

	public boolean getDeleted(){
		return this.deleted;
	}

	public void setDeleted(boolean deleted){
		this.deleted = deleted;
	}

	public long getNumber(){
		return  this.number;
	}

	public void setNumber(long number){
		this.number = number;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if ( (o == null) || (getClass() != o.getClass()) ) return false;
		Status status = (Status) o;

		if(!Objects.equals(this.id, status.id)) return false;
		if(!Objects.equals(this.name, status.name)) return false;
		if(!Objects.equals(this.number, status.number)) return false;
		return Objects.equals(this.deleted, status.deleted);
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 42 * result + Objects.hashCode(this.name);
		result = 42 * result + Objects.hashCode(this.number);
		result = 42 * result + Objects.hashCode(this.deleted);
		return result;
	}
}
