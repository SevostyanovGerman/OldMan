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
@Table(name = "product")
public class Product {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = RegexpConstans.REG_EXP_OF_FIRST_NAME, message = "{name.wrong}")
	@Column(name = "product_name", length = 50)
	private String productName;

	@Column(name = "deleted")
	private Boolean deleted;

	public Product() {
		this.deleted = false;
	}

	public Product(String productName) {
		this.productName = productName;
		this.deleted = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return productName;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Product product = (Product) o;

		if (id != null ? !id.equals(product.id) : product.id != null) {
			return false;
		}
		if (productName != null ? !productName.equals(product.productName) : product.productName != null) {
			return false;
		}
		return deleted != null ? deleted.equals(product.deleted) : product.deleted == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (productName != null ? productName.hashCode() : 0);
		result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
		return result;
	}
}
