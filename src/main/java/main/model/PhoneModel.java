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
@Table(name = "phone_model")
public class PhoneModel {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = RegexpConstans.REG_EXP_OF_MODEL_PHONE, message = "{name.wrong}")
	@Column(name = "model_name", length = 50)
	private String modelName;

	@Column(name = "deleted")
	private Boolean deleted;

	public PhoneModel() {
		this.deleted = false;
	}

	public PhoneModel(String modelName) {
		this.modelName = modelName;
		this.deleted = false;
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

		PhoneModel that = (PhoneModel) o;

		if (id != null ? !id.equals(that.id) : that.id != null) {
			return false;
		}
		if (modelName != null ? !modelName.equals(that.modelName) : that.modelName != null) {
			return false;
		}
		return deleted != null ? deleted.equals(that.deleted) : that.deleted == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (modelName != null ? modelName.hashCode() : 0);
		result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
		return result;
	}
}
