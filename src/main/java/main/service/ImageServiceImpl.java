package main.service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import main.Helpers;
import main.model.Image;
import main.model.ImageType;
import main.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

	private static final int IMG_WIDTH = 640;

	/*Важно! Значения CUSTOMER_FILES и DESIGNER_FILES должны совподать с атрибутом name
	тега input для загрузки файлов на страницах ManagerItemForm.html & DesignerItemForm.html
	*/
	private static final String CUSTOMER_FILES = "uploadCustomerFiles";
	private static final String DESIGNER_FILES = "uploadDesignerFiles";

	private ImageRepository imageRepository;

	@Autowired
	public ImageServiceImpl(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	private final static Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

	@Override
	public void save(Image image) {
		imageRepository.save(image);
	}

	@Override
	public void delete(Image image) {
		imageRepository.delete(image);
	}

	@Override
	public Image get(Long id) {
		return imageRepository.findById(id);
	}

	@Override
	public List<Image> uploadAndSaveBlobFile(MultipartHttpServletRequest uploadFiles) throws IOException, SQLException {
		List<Image> blobFileList = new ArrayList<>();
		String imageType;
		List<MultipartFile> fileList;
		if (uploadFiles.getMultiFileMap().containsKey(CUSTOMER_FILES)) {
			imageType = ImageType.CUSTOMER.toString();
			fileList = uploadFiles.getFiles(CUSTOMER_FILES);
		} else {
			imageType = ImageType.DESIGNER.toString();
			fileList = uploadFiles.getFiles(DESIGNER_FILES);
		}
		for (MultipartFile file : fileList) {
			if (!file.isEmpty()) {
				BufferedImage resizedImage;
				BufferedImage originalImage = ImageIO.read(new BufferedInputStream(file.getInputStream()));
				if (originalImage.getWidth() > IMG_WIDTH) {
					try {
						resizedImage = Helpers.resizePicture(originalImage, originalImage.getType(), IMG_WIDTH);
					} catch (Exception e) {
						resizedImage = originalImage;
					}
				} else {
					resizedImage = originalImage;
				}
				String filename = file.getOriginalFilename();
				String type = "jpg";
				if (filename.lastIndexOf(".") != -1) {
					 type = filename.substring(filename.lastIndexOf(".")+1, filename.length());
				}


				Blob resizedFile = new SerialBlob(Helpers.convertToByteArray(resizedImage, type));
				Blob originalFile = new SerialBlob(file.getBytes());
				Image newImage = new Image();
				newImage.setImageType(imageType);
				newImage.setFileName(file.getOriginalFilename());
				newImage.setSmallImage(resizedFile);
				newImage.setImage(originalFile);
				newImage.setFormatType(type);
				save(newImage);
				blobFileList.add(newImage);
			}
		}
		return blobFileList;
	}

	@Override
	public void downloadAllFiles(List<Image> downloadFiles) throws IOException, SQLException {
		String pathDownloadDirectory;
		if (downloadFiles.size() != 0) {
			String typeOperationSystem = System.getProperty("os.name");
			if (typeOperationSystem.contains("Windows")) {
				pathDownloadDirectory = System.getProperty("user.home") + "\\Downloads\\";
			} else {
				pathDownloadDirectory = System.getProperty("user.home") + "/Downloads/";
			}
			for (Image downloadFile : downloadFiles) {
				InputStream inputStream = downloadFile.getBlobFile().getBinaryStream();
				FileOutputStream fileOutputStream = new FileOutputStream(
					pathDownloadDirectory + downloadFile.getFileName());
				int readByte;
				while ((readByte = inputStream.read()) != -1) {
					fileOutputStream.write(readByte);
				}
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
	}

	@Override
	public void downloadOneFile(Image downloadFile) throws IOException, SQLException {
		String pathDownloadDirectory;
		if (downloadFile != null) {
			String typeOperationSystem = System.getProperty("os.name");
			if (typeOperationSystem.contains("Windows")) {
				pathDownloadDirectory = System.getProperty("user.home") + "\\Downloads\\";
			} else {
				pathDownloadDirectory = System.getProperty("user.home") + "/Downloads/";
			}
			InputStream inputStream = downloadFile.getBlobFile().getBinaryStream();
			FileOutputStream fileOutputStream = new FileOutputStream(
				pathDownloadDirectory + downloadFile.getFileName());
			int readByte;
			while ((readByte = inputStream.read()) != -1) {
				fileOutputStream.write(readByte);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
		}
	}

	@Override
	public File zipFiles(List<Image> downloadFiles) throws IOException, SQLException {
		File zipFile = new File("archive.zip");
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		for (Image downloadFile : downloadFiles) {
			InputStream inputStream = downloadFile.getBlobFile().getBinaryStream();
			File srcFile = new File(downloadFile.getFileName());
			zos.putNextEntry(new ZipEntry(srcFile.getName()));
			int readByte;
			while ((readByte = inputStream.read()) != -1) {
				zos.write(readByte);
			}
			zos.closeEntry();
		}
		zos.close();
		logger.debug("Files compressed to zip");
		return zipFile;
	}
}
