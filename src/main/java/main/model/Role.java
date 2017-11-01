package main.model;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "def_url")
	private String url;

	@Column(name = "deleted")
	private boolean deleted;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getAuthority() {
		return name;
	}

	@Override
	public String toString() {
		return name;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if ( (o == null) || (getClass() != o.getClass()) ) return false;
		Role role = (Role) o;

		if(!Objects.equals(this.id, role.id)) return false;
		if(!Objects.equals(this.name, role.name)) return false;
		if(!Objects.equals(this.url, role.url)) return false;
		return Objects.equals(this.deleted, role.deleted);
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 42 * result + Objects.hashCode(this.name);
		result = 42 * result + Objects.hashCode(this.url);
		result = 42 * result + Objects.hashCode(this.deleted);
		return result;
	}
}
