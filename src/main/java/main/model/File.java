package main.model;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "url")
	private String url;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		File file = (File) o;
		if (id != null ? !id.equals(file.id) : file.id != null) return false;
		return url != null ? url.equals(file.url) : file.url == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (url != null ? url.hashCode() : 0);
		return result;
	}
}

