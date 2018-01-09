package main.model;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "items")
public class Item {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "model_id")
	private PhoneModel phoneModel;

	@Column(name = "material")
	private String material;

	@Column(name = "comment")
	private String comment;

	@Column(name = "count")
	private int count;

	@Column(name = "price")
	private double price;

	@Column(name = "status")
	private boolean status; //Статус готовности позиции заказа

	@Column(name = "amount")
	private Double amount;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "item_id")
	@Where(clause = "image_type='CUSTOMER'")
	private List<Image> files;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "item_id")
	@Where(clause = "image_type='DESIGNER'")
	private List<Image> images;

	public Item() {
	}

	public Item(Product product, PhoneModel phoneModel, String material, String comment, int count, double price,
		boolean status) {
		this.product = product;
		this.phoneModel = phoneModel;
		this.material = material;
		this.comment = comment;
		this.count = count;
		this.price = price;
		this.status = status;
		this.amount = count * price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public PhoneModel getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(PhoneModel phoneModel) {
		this.phoneModel = phoneModel;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<Image> getFiles() {
		return files;
	}

	public void setFiles(List<Image> files) {
		this.files = files;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public boolean getStatus() {
		return status;
	}

	public String getStatusString() {
		if (status) {
			return "готов";
		}
		return "не готов";
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public boolean isStatus() {
		return status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Item)) {
			return false;
		}
		Item item = (Item) o;
		return count == item.count && Double.compare(item.price, price) == 0 && status == item.status && Objects
			.equals(id, item.id) && Objects.equals(product, item.product) && Objects.equals(phoneModel, item.phoneModel)
			&& Objects.equals(material, item.material) && Objects.equals(comment, item.comment) && Objects
			.equals(amount, item.amount) && Objects.equals(files, item.files) && Objects.equals(images, item.images);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, product, phoneModel, material, comment, count, price, status, amount, files, images);
	}
}
