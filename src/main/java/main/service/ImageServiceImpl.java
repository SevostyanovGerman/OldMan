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
import java.sql.Blob;
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
		for (int i = 0; i < files.size() ; i++)  {
			MultipartFile file = files.get(i);
			if (!file.isEmpty()) {
				try {
					name = file.getOriginalFilename();
					byte[] bytes = file.getBytes();
					Blob blob = new SerialBlob(bytes);
					Image image = new Image(blob);
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
	public List<Image> uploadAndSaveBlobFile(MultipartHttpServletRequest uploadFiles) {
		List<Image> blobFilesList = new ArrayList<>();
		List<MultipartFile> fileList = uploadFiles.getFiles("uploadCustomerFiles");
		for (MultipartFile file : fileList) {
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();
					Image newFile = new Image();
					newFile.setType(true);
					Blob blobFile = new SerialBlob(bytes);
					newFile.setImage(blobFile);
					imageService.save(newFile);
					blobFilesList.add(newFile);
					logger.info("Вы удачно загрузили файл {}", file.getOriginalFilename());
				} catch (Exception e) {
					logger.info("Ошибка при загрузке файла");
				}
			}
		}
		return blobFilesList;
	}
}
