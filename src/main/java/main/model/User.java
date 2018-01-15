package main.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.DatatypeConverter;
import main.constans.RegexpConstans;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Pattern(regexp = RegexpConstans.REG_EXP_OF_LOGIN, message = "{user.login.wrong}")
	@Column(name = "name", nullable = false, length = 16)
	private String name;

	@NotNull
	@Size(min = 3, message = "{user.password.wrong}")
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "deleted")
	private boolean deleted;

	@Column(name = "disable")
	private boolean disable;

	@Pattern(regexp = RegexpConstans.REG_EXP_OF_FIRST_NAME, message = "{user.firstname.wrong}")
	@Column(name = "first_name", length = 50, nullable = false)
	private String firstName;

	@Size(max = 50, message = "{user.secname.wrong.size}")
	@Pattern(regexp = RegexpConstans.REF_EXP_OF_SECOND_NAME, message = "{user.secname.wrong.pattern}")
	@Column(name = "sec_name", length = 50)
	private String secName;

	@Column(name = "position")
	private String position;

	@Column(name = "creation_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@NotNull
	@Email(message = "{user.email.wrong}")
	@Column(name = "email", nullable = false)
	private String email;

	@Pattern(regexp = RegexpConstans.REG_EXP_OF_PHONE, message = "{user.phone.wrong}")
	@Column(name = "phone", nullable = false, length = 20)
	private String phone;

	@Column(name = "avatar")
	private Blob avatar;

	@Column(name = "token")
	private String token;

	@Column(name = "tokenExpire")
	private Date tokenExpire;

	@Size(min = 1, message = "{user.roles.wrong}")
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class)
	@JoinTable(name = "permissions", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {
		@JoinColumn(name = "role_id")})
	private Set<Role> roles;

	public User() {
		this.created = new Date();
	}

	public User(String name, String firstName, String secName, boolean deleted, boolean disable, Role role,
		String email, String phone) {
		this.name = name;
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
		this.created = new Date();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getTokenExpire() {
		return tokenExpire;
	}

	public void setTokenExpire(Date tokenExpire) {
		this.tokenExpire = tokenExpire;
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

	public boolean isDeleted() {
		return deleted;
	}

	public boolean isDisable() {
		return disable;
	}

	public String getAvatar() throws IOException, SQLException {
		if (avatar != null) {
			return "data:image/jpg;base64," + convertToBase64(this.avatar.getBinaryStream());
		} else {
			return null;
		}
	}

	private String convertToBase64(InputStream inputStream) throws IOException {
		BufferedImage image = ImageIO.read(inputStream);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		return DatatypeConverter.printBase64Binary(baos.toByteArray());
	}

	public void setAvatar(Blob avatar) {
		this.avatar = avatar;
	}

	public Blob getAvatarBlob(){
		return this.avatar;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		User user = (User) o;

		if (id != user.id) {
			return false;
		}
		if (deleted != user.deleted) {
			return false;
		}
		if (disable != user.disable) {
			return false;
		}
		if (name != null ? !name.equals(user.name) : user.name != null) {
			return false;
		}
		if (password != null ? !password.equals(user.password) : user.password != null) {
			return false;
		}
		if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) {
			return false;
		}
		if (secName != null ? !secName.equals(user.secName) : user.secName != null) {
			return false;
		}
		if (position != null ? !position.equals(user.position) : user.position != null) {
			return false;
		}
		if (created != null ? !created.equals(user.created) : user.created != null) {
			return false;
		}
		if (email != null ? !email.equals(user.email) : user.email != null) {
			return false;
		}
		if (phone != null ? !phone.equals(user.phone) : user.phone != null) {
			return false;
		}
		if (avatar != null ? !avatar.equals(user.avatar) : user.avatar != null) {
			return false;
		}
		if (token != null ? !token.equals(user.token) : user.token != null) {
			return false;
		}
		if (tokenExpire != null ? !tokenExpire.equals(user.tokenExpire) : user.tokenExpire != null) {
			return false;
		}
		return roles != null ? roles.equals(user.roles) : user.roles == null;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (deleted ? 1 : 0);
		result = 31 * result + (disable ? 1 : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (secName != null ? secName.hashCode() : 0);
		result = 31 * result + (position != null ? position.hashCode() : 0);
		result = 31 * result + (created != null ? created.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
		result = 31 * result + (token != null ? token.hashCode() : 0);
		result = 31 * result + (tokenExpire != null ? tokenExpire.hashCode() : 0);
		result = 31 * result + (roles != null ? roles.hashCode() : 0);
		return result;
	}
}
