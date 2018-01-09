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
@Table(name = "phone_model")
public class PhoneModel {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = RegexpConstans.REG_EXP_OF_MODEL_PHONE, message = "{name.wrong}")
	@Column(name = "model_name", length = 50)
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
		if (this == o) {
			return true;
		}
		if (!(o instanceof PhoneModel)) {
			return false;
		}
		PhoneModel that = (PhoneModel) o;
		return Objects.equals(id, that.id) && Objects.equals(modelName, that.modelName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, modelName);
	}
}
