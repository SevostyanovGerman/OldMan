package main.service;

import main.model.Image;
import main.model.Item;
import main.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

	private static final int IMG_WIDTH = 640;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	ImageService imageService;

	@Autowired
	ItemService itemService;

	private final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

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
	public void saveBlobImagesToItem(List<MultipartFile> files, Long itemId) {
		String name;
		Item item = itemService.get(itemId);
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				try {
					name = file.getOriginalFilename();
					byte[] bytes = file.getBytes();
					Blob blob = new SerialBlob(bytes);
					Image image = new Image(blob);
					image.setFileName(name);
					image.setType(false);
					imageService.save(image);
					item.getImages().add(image);
					itemService.save(item);
					logger.info("Вы удачно загрузили файл {}", name);
				} catch (Exception e) {
					logger.info("Ошибка при загрузке файла");
				}
			}
		}
	}

	@Override
	public List<Image> uploadAndSaveBlobFile(MultipartHttpServletRequest uploadFiles)
		throws IOException, SQLException {
		List<Image> blobFileList = new ArrayList<>();
		List<MultipartFile> fileList = uploadFiles.getFiles("uploadCustomerFiles");
		for (MultipartFile file : fileList) {
			if (!file.isEmpty()) {


				BufferedImage originalImage = ImageIO.read(convertToFile(file));
				BufferedImage resizedImage = resizePicture(originalImage, originalImage.getType());
				Blob resizedFile = new SerialBlob(convertToByteArray(resizedImage));

				//byte[] bytes = file.getBytes();
				//Blob blobFile = new SerialBlob(bytes);
				Blob originalFile = new SerialBlob(file.getBytes());

				Image newFile = new Image();
				newFile.setType(true);
				newFile.setFileName(file.getOriginalFilename());
				newFile.setSmallImage(resizedFile);
				//newFile.setImage(blobFile);
				newFile.setImage(originalFile);
				imageService.save(newFile);
				blobFileList.add(newFile);
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
		}
	}

	private File convertToFile(MultipartFile file) throws IOException {
		File convertedFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertedFile);
		fos.write(file.getBytes());
		fos.close();
		return convertedFile;
	}

	private byte[] convertToByteArray(BufferedImage bufferedImage) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", baos);
		baos.flush();
		byte[] bytes = baos.toByteArray();
		baos.close();
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
