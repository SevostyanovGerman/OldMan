package main.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "deleted")
	private boolean deleted;

	@Column(name = "disable")
	private boolean disable;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "sec_name")
	private String secName;

	@Column(name = "position")
	private String position;

	@Column(name = "creation_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "phone", nullable = false)
	private String phone;

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class)
	@JoinTable(name = "permissions", joinColumns = {@JoinColumn(name = "user_id")},
		inverseJoinColumns = {@JoinColumn(name = "role_id")})
	private Set<Role> roles;

	public User() {
	}

	public User(String name, String password, String firstName, String secName, boolean deleted, boolean disable,
				Role role, String email, String phone) {
		this.name = name;
		this.password = password;
		this.firstName = firstName;
		this.secName = secName;
		this.phone = phone;
		this.email = email;
		this.deleted = deleted;
		this.disable = disable;
		if (this.roles == null) {
			Set<Role> list = new HashSet<>();
			list.add(role);
			this.roles = list;
		} else {
			this.roles.add(role);
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean getDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecName() {
		return secName;
	}

	public void setSecName(String secName) {
		this.secName = secName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCreated() {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		return dateFormat.format(created);
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return firstName + " " + secName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		if (!Objects.equals(this.id, user.id)) return false;
		if (!Objects.equals(this.deleted, user.deleted)) return false;
		if (!Objects.equals(this.disable, user.disable)) return false;
		if (!Objects.equals(this.name, user.name)) return false;
		if (!Objects.equals(this.password, user.password)) return false;
		if (!Objects.equals(this.firstName, user.firstName)) return false;
		if (!Objects.equals(this.secName, user.secName)) return false;
		if (!Objects.equals(this.position, user.position)) return false;
		if (!Objects.equals(this.created, user.created)) return false;
		return Objects.equals(this.roles, user.roles);
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + Objects.hashCode(this.name);
		result = 31 * result + Objects.hashCode(this.password);
		result = 31 * result + Objects.hashCode(this.deleted);
		result = 31 * result + Objects.hashCode(this.disable);
		result = 31 * result + Objects.hashCode(this.firstName);
		result = 31 * result + Objects.hashCode(this.secName);
		result = 31 * result + Objects.hashCode(this.position);
		result = 31 * result + Objects.hashCode(this.created);
		return result;
	}
}
