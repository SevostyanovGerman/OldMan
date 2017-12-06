package main.model;

import javax.persistence.*;

@Entity
@Table(name = "function")
public class FuncMenu {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "link")
	private String link;

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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public FuncMenu() {
	}

	public FuncMenu(String name, String link) {
		this.name = name;
		this.link = link;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FuncMenu function = (FuncMenu) o;
		if (id != null ? !id.equals(function.id) : function.id != null) {
			return false;
		}
		if (name != null ? !name.equals(function.name) : function.name != null) {
			return false;
		}
		return link != null ? link.equals(function.link) : function.link == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (link != null ? link.hashCode() : 0);
		return result;
	}
}
