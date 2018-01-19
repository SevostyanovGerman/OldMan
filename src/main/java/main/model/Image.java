package main.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.DatatypeConverter;

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

	@Column(name = "image_type")
	private String imageType;

	@Column(name = "file_name")
	private String fileName;

	//расширение файла
	@Column(name = "formatType")
	private String formatType;

	@Column(name = "isImage")
	private boolean isImage;

	public Image() {

		this.isImage = false;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
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
		try {
			if (this.image != null) {
				if (isImage) {
					return "data:image/" + this.formatType + ";base64," + convertToBase64(this.image.getBinaryStream(),
						this.formatType);
				} else {
					byte[] bytes = this.image.getBytes(1, (int) this.image.length());
					String encodedURL = java.util.Base64.getEncoder().encodeToString(bytes);
					return "data:file/" + this.formatType + ";base64," + encodedURL;
				}
			}
		} catch (Exception e) {

		}
		return null;
	}

	public String getSmallImage() throws IOException, SQLException {
		if (this.smallImage != null) {
			if (isImage) {
				return "data:image/" + this.formatType + ";base64," + convertToBase64(this.smallImage.getBinaryStream(),
					this.formatType);
			} else {
				return null;
			}
		}
		return null;
	}

	private String convertToBase64(InputStream inputStream, String type) throws IOException {

		BufferedImage image = ImageIO.read(inputStream);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (type == null) {
			type = "jpg";
		}
		ImageIO.write(image, type, baos);
		return DatatypeConverter.printBase64Binary(baos.toByteArray());
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	public boolean isImage() {
		return isImage;
	}

	public void setIsImage(boolean image) {
		isImage = image;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Image image1 = (Image) o;

		if (isImage != image1.isImage) {
			return false;
		}
		if (id != null ? !id.equals(image1.id) : image1.id != null) {
			return false;
		}
		if (smallImage != null ? !smallImage.equals(image1.smallImage) : image1.smallImage != null) {
			return false;
		}
		if (image != null ? !image.equals(image1.image) : image1.image != null) {
			return false;
		}
		if (imageType != null ? !imageType.equals(image1.imageType) : image1.imageType != null) {
			return false;
		}
		if (fileName != null ? !fileName.equals(image1.fileName) : image1.fileName != null) {
			return false;
		}
		return formatType != null ? formatType.equals(image1.formatType) : image1.formatType == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (smallImage != null ? smallImage.hashCode() : 0);
		result = 31 * result + (image != null ? image.hashCode() : 0);
		result = 31 * result + (imageType != null ? imageType.hashCode() : 0);
		result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
		result = 31 * result + (formatType != null ? formatType.hashCode() : 0);
		result = 31 * result + (isImage ? 1 : 0);
		return result;
	}
}
