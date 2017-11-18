package main.service;

import main.model.Image;
import main.model.Item;
import main.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import java.sql.Blob;
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
	public void del(Image image) {
		imageRepository.delete(image);
	}

	@Override
	public Image get(Long id) {
		return imageRepository.findById(id);
	}

	@Override
	public boolean saveBlobImage(List<MultipartFile> files, Long itemId) {
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
					imageService.save(image);
					item.getImages().add(image);
					itemService.save(item);
					logger.info("Вы удачно загрузили файл {}", name);
				} catch (Exception e) {
					logger.info("Ошибка при загрузке файла");
					return false;
				}
			}
		}
		return true;
	}
}
