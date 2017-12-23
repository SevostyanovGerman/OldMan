package main.model;

import javax.persistence.*;
import java.util.Objects;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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

	public Product() {
	}

	public Product(String productName) {
		this.productName = productName;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Product)) return false;
		Product product = (Product) o;
		return Objects.equals(id, product.id) &&
			Objects.equals(productName, product.productName);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, productName);
	}
}
