package main.model;


import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

@Entity
@Table(name = "files")
public class File {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "blob_file")
	private Blob file;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Item.class)
	@JoinTable(name = "keys_item_file", joinColumns = {@JoinColumn(name = "file_id")},
		inverseJoinColumns = {@JoinColumn(name = "item_id")})
	private Item item;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFile() throws IOException, SQLException {
		if (this.file != null) {
			InputStream in = this.file.getBinaryStream();
			BufferedImage image = ImageIO.read(in);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			String data = DatatypeConverter.printBase64Binary(baos.toByteArray());
			return "data:image/png;base64," + data;
		}
		return null;
	}

	public void setFile(Blob file) {
		this.file = file;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof File)) return false;
		File file1 = (File) o;
		if (id != null ? !id.equals(file1.id) : file1.id != null) return false;
		return file != null ? file.equals(file1.file) : file1.file == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (file != null ? file.hashCode() : 0);
		return result;
	}
}

