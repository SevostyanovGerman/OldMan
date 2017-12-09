package main.model;

import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

	@Column(name = "small_image")
	private Blob smallImage;

	@Column(name = "image")
	private Blob image;

	@Column(name = "type")
	private boolean type;

	@Column(name = "file_name")
	private String fileName;

	public Image() {
	}

	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public Image(Blob image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Blob getSmallBlobFile() {
		return smallImage;
	}

	public void setSmallImage(Blob smallImage) {
		this.smallImage = smallImage;
	}

	public Blob getBlobFile() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public String getImage() throws IOException, SQLException {
		return "data:image/jpg;base64," + convertToBase64(this.image.getBinaryStream());
	}

	public String getSmallImage() throws IOException, SQLException {
		return "data:image/jpg;base64," + convertToBase64(this.smallImage.getBinaryStream());
	}

	private String convertToBase64(InputStream inputStream) throws IOException {
		BufferedImage image = ImageIO.read(inputStream);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		return DatatypeConverter.printBase64Binary(baos.toByteArray());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Image)) {
			return false;
		}
		Image image1 = (Image) o;
		if (id != null ? !id.equals(image1.id) : image1.id != null) {
			return false;
		}
		if (image != null ? !image.equals(image1.image) : image1.image != null) {
			return false;
		}
		return fileName != null ? fileName.equals(image1.fileName) : image1.fileName == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (image != null ? image.hashCode() : 0);
		result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
		return result;
	}
}
