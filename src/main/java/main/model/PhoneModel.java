package main.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "phone_model")
public class PhoneModel {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "model_name")
	private String modelName;

	public PhoneModel() {
	}

	public PhoneModel(String modelName) {
		this.modelName = modelName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@Override
	public String toString() {
		return modelName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PhoneModel)) return false;
		PhoneModel that = (PhoneModel) o;
		return Objects.equals(id, that.id) &&
			Objects.equals(modelName, that.modelName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, modelName);
	}
}
