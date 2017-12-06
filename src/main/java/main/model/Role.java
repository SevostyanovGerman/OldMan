package main.model;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority, Comparable<Role> {

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

	@ManyToMany(mappedBy = "roles")
	private List<User> users;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "keys_roles_functions", joinColumns = {@JoinColumn(name = "role_id")},
			   inverseJoinColumns = {@JoinColumn(name = "function_id")})
	private List<FuncMenu> functions;

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Status.class)
	@JoinTable(name = "status_access", joinColumns = {@JoinColumn(name = "role_id")},
			   inverseJoinColumns = {@JoinColumn(name = "status_id")})
	private Set<Status> statuses;

	public Role() {
	}

	public Role(String name, List<FuncMenu> functions) {
		this.name = name;
		this.functions = functions;
	}

	public Role(String name, String url, Status status) {
		this.name = name;
		this.url = url;
		if (this.statuses == null) {
			Set<Status> setStatuses = new HashSet<>();
			setStatuses.add(status);
			this.statuses = setStatuses;
		} else {
			this.statuses.add(status);
		}
		this.functions = new ArrayList<>();
	}

	public Role(String name, String url, HashSet<Status> statuses) {
		this.name = name;
		this.url = url;
		this.statuses = statuses;
		this.functions = new ArrayList<>();
	}

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
		return this.id;
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

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public boolean getDeleted() {
		return this.deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Set<Status> getStatuses() {
		return this.statuses;
	}

	public void setStatuses(Set<Status> statuses) {
		this.statuses = statuses;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public List<FuncMenu> getFunctions() {
		return functions;
	}

	public void setFunctions(List<FuncMenu> functions) {
		this.functions = functions;
	}

	@Override
	public int compareTo(Role o) {
		return this.name.compareTo(o.name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Role role = (Role) o;
		if (id != role.id) {
			return false;
		}
		if (deleted != role.deleted) {
			return false;
		}
		if (name != null ? !name.equals(role.name) : role.name != null) {
			return false;
		}
		if (url != null ? !url.equals(role.url) : role.url != null) {
			return false;
		}
		if (users != null ? !users.equals(role.users) : role.users != null) {
			return false;
		}
		if (functions != null ? !functions.equals(role.functions) : role.functions != null) {
			return false;
		}
		return statuses != null ? statuses.equals(role.statuses) : role.statuses == null;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (url != null ? url.hashCode() : 0);
		result = 31 * result + (deleted ? 1 : 0);
		result = 31 * result + (users != null ? users.hashCode() : 0);
		result = 31 * result + (functions != null ? functions.hashCode() : 0);
		result = 31 * result + (statuses != null ? statuses.hashCode() : 0);
		return result;
	}
}
