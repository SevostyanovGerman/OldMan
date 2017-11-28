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
import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

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
	public void saveBlobImage(List<MultipartFile> files, Long itemId) {
		String name;
		Item item = itemService.get(itemId);
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				try {
					name = file.getOriginalFilename();
					byte[] bytes = file.getBytes();
					Blob blob = new SerialBlob(bytes);
					Image image = new Image(blob);
					image.setFileName(file.getOriginalFilename());
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
	public List<Image> uploadAndSaveBlobFile(MultipartHttpServletRequest uploadFiles) throws IOException, SQLException {
		List<Image> blobFileList = new ArrayList<>();
		List<MultipartFile> fileList = uploadFiles.getFiles("uploadCustomerFiles");
		for (MultipartFile file : fileList) {
			if (!file.isEmpty()) {
				byte[] bytes = file.getBytes();
				Image newFile = new Image();
				newFile.setType(true);
				newFile.setFileName(file.getOriginalFilename());
				Blob blobFile = new SerialBlob(bytes);
				newFile.setImage(blobFile);
				imageService.save(newFile);
				blobFileList.add(newFile);
			}
		}
		return blobFileList;
	}

	@Override
	public void downloadAllFiles(List<Image> downloadFiles) throws IOException, SQLException {
		String pathDownloadDirectory;
		if (downloadFiles.size()!=0){
			String typeOperationSystem = System.getProperty("os.name");
			if(typeOperationSystem.contains("Windows")){
				pathDownloadDirectory = System.getProperty("user.home") + "\\Downloads\\";
			}else{
				pathDownloadDirectory = System.getProperty("user.home") + "/Downloads/";
			}
			for (Image downloadFile: downloadFiles){
				InputStream inputStream = downloadFile.getBlobFile().getBinaryStream();
				FileOutputStream fileOutputStream = new FileOutputStream(pathDownloadDirectory + downloadFile.getFileName());
				int readByte;
				while ((readByte = inputStream.read()) != -1){
					fileOutputStream.write(readByte);
				}
			}
		}
	}

	@Override
	public void downloadOneFile(Image downloadFile) throws IOException, SQLException {
		String pathDownloadDirectory;
		if (downloadFile!=null) {
			String typeOperationSystem = System.getProperty("os.name");
			if (typeOperationSystem.contains("Windows")) {
				pathDownloadDirectory = System.getProperty("user.home") + "\\Downloads\\";
			} else {
				pathDownloadDirectory = System.getProperty("user.home") + "/Downloads/";
			}
			InputStream inputStream = downloadFile.getBlobFile().getBinaryStream();
			FileOutputStream fileOutputStream = new FileOutputStream(pathDownloadDirectory + downloadFile.getFileName());
			int readByte;
			while ((readByte = inputStream.read()) != -1){
				fileOutputStream.write(readByte);
			}
		}
	}
}
