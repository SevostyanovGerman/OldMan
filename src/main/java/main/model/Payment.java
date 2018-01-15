package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import main.constans.RegexpConstans;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = RegexpConstans.REG_EXP_GENERAL, message = "{general.wrong.message}")
	@Column(name = "name")
	private String name;

	// Данная переменная сигнализирует может ли этот способ оплаты приниматься производителем
	@Column(name = "cash")
	private Boolean cash;

	@Column(name = "deleted")
	private Boolean deleted;

	public Payment(String name, Boolean cash) {
		this.name = name;
		this.cash = cash;
		this.deleted = false;
	}

	public Payment() {
		this.deleted = false;
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

	public Boolean getCash() {
		return cash;
	}

	public void setCash(Boolean cash) {
		this.cash = cash;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Payment payment = (Payment) o;

		if (id != null ? !id.equals(payment.id) : payment.id != null) {
			return false;
		}
		if (name != null ? !name.equals(payment.name) : payment.name != null) {
			return false;
		}
		if (cash != null ? !cash.equals(payment.cash) : payment.cash != null) {
			return false;
		}
		return deleted != null ? deleted.equals(payment.deleted) : payment.deleted == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (cash != null ? cash.hashCode() : 0);
		result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
		return result;
	}
}
