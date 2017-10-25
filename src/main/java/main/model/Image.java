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
@Table(name = "images")
public class Image {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "image")
	private Blob image;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImage() throws IOException, SQLException {
		if (this.image != null) {
			InputStream in = this.image.getBinaryStream();
			BufferedImage image = ImageIO.read(in);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			String data = DatatypeConverter.printBase64Binary(baos.toByteArray());
			String imageString = "data:image/png;base64," + data;
			String html = imageString;
			return html;
		}
		return null;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Image image1 = (Image) o;
		if (id != null ? !id.equals(image1.id) : image1.id != null) return false;
		return image != null ? image.equals(image1.image) : image1.image == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (image != null ? image.hashCode() : 0);
		return result;
	}
}
