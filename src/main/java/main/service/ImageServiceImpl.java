package main.service;

import main.model.Image;
import main.model.ImageType;
import main.model.Item;
import main.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

	private static final int IMG_WIDTH = 640;

	/*Важно! Значения CUSTOMER_FILES и DESIGNER_FILES должны совподать с атрибутом name
	тега input для загрузки файлов на страницах ManagerItemForm.html & DesignerItemForm.html
	*/
	private static final String CUSTOMER_FILES = "uploadCustomerFiles";
	private static final String DESIGNER_FILES = "uploadDesignerFiles";

	@Autowired
	private ImageRepository imageRepository;

	private ItemService itemService;

	@Autowired
	public ImageServiceImpl(ImageRepository imageRepository,
							ItemService itemService) {
		this.imageRepository = imageRepository;
		this.itemService = itemService;
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
	public List<Image> uploadAndSaveBlobFile(MultipartHttpServletRequest uploadFiles)
		throws IOException, SQLException {
		List<Image> blobFileList = new ArrayList<>();
		String imageType;
		List<MultipartFile> fileList;
		if(uploadFiles.getMultiFileMap().containsKey(CUSTOMER_FILES)){
			imageType = ImageType.CUSTOMER.toString();
			fileList = uploadFiles.getFiles(CUSTOMER_FILES);
		}else {
			imageType = ImageType.DESIGNER.toString();
			fileList = uploadFiles.getFiles(DESIGNER_FILES);
		}
		for (MultipartFile file : fileList) {
			if (!file.isEmpty()) {
				BufferedImage resizedImage;
				BufferedImage originalImage = ImageIO.read(new BufferedInputStream(file.getInputStream()));
				if (originalImage.getWidth()>IMG_WIDTH){
					resizedImage = resizePicture(originalImage, originalImage.getType());
				}else {
					resizedImage = originalImage;
				}
				Blob resizedFile = new SerialBlob(convertToByteArray(resizedImage));
				Blob originalFile = new SerialBlob(file.getBytes());
				Image newImage = new Image();
				newImage.setImageType(imageType);
				newImage.setFileName(file.getOriginalFilename());
				newImage.setSmallImage(resizedFile);
				newImage.setImage(originalFile);
				imageService.save(newImage);
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
				FileOutputStream fileOutputStream =
					new FileOutputStream(pathDownloadDirectory + downloadFile.getFileName());
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
			FileOutputStream fileOutputStream =
				new FileOutputStream(pathDownloadDirectory + downloadFile.getFileName());
			int readByte;
			while ((readByte = inputStream.read()) != -1) {
				fileOutputStream.write(readByte);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
		}
	}

	private byte[] convertToByteArray(BufferedImage bufferedImage) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", outputStream);
		outputStream.flush();
		byte[] bytes = outputStream.toByteArray();
		outputStream.close();
		return bytes;
	}

	private BufferedImage resizePicture(BufferedImage originalImage, int type){
		int widthImage = originalImage.getWidth();
		int highImage = originalImage.getHeight();
		int resizedHigh = (highImage*IMG_WIDTH)/widthImage;
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, resizedHigh, type);
		Graphics2D graphics = resizedImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics.drawImage(originalImage, 0, 0, IMG_WIDTH, resizedHigh, null);
		return resizedImage;
	}
}
