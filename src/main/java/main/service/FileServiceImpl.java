package main.service;

import main.model.File;
import main.model.Item;
import main.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService{

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private ItemService itemService;

	private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public void save(File file) {
		fileRepository.save(file);
	}

	@Override
	public void delete(File file) {
		fileRepository.delete(file);
	}

	@Override
	public File get(Long id) {
		return fileRepository.getOne(id);
	}

	@Override
	public boolean saveBlobFile(MultipartHttpServletRequest multipartRequest, Long itemId) {
		String name;
		Item item = itemService.get(itemId);
		for (Map.Entry<String, MultipartFile> set : multipartRequest.getFileMap().entrySet()) {
			MultipartFile file = set.getValue();
			if (!file.isEmpty()) {
				try {
					name = file.getOriginalFilename();
					byte[] bytes = file.getBytes();
					File newFile = new File();
					Blob blob = new SerialBlob(bytes);
					newFile.setFile(blob);
					fileService.save(newFile);
					item.getFiles().add(newFile);
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
