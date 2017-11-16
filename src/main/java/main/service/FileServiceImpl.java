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
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
	public List<File> saveBlobFile(MultipartHttpServletRequest multipartRequest) {
		List<File> blobFilesList = new ArrayList<>();
		for (Map.Entry<String, MultipartFile> set : multipartRequest.getFileMap().entrySet()) {
			MultipartFile file = set.getValue();
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();
					File newFile = new File();
					Blob blobFile = new SerialBlob(bytes);
					newFile.setFile(blobFile);
					fileService.save(newFile);
					blobFilesList.add(newFile);
					logger.info("Вы удачно загрузили файл {}", file.getOriginalFilename());
				} catch (Exception e) {
					logger.info("Ошибка при загрузке файла");
				}
			}
		}
		return blobFilesList;
	}

	@Override
	public List<File> uploadFile(MultipartFile[] uploadCustomerFiles) throws SQLException, IOException {
		List<File> uploadFilesList = new ArrayList<>();
		for (MultipartFile uploadedFile : uploadCustomerFiles) {
			if (!uploadedFile.isEmpty()) {
				byte[] bytes = uploadedFile.getBytes();
				File newFile = new File();
				Blob blobFile = new SerialBlob(bytes);
				newFile.setFile(blobFile);
				fileService.save(newFile);
				uploadFilesList.add(newFile);
			}
		}
		return uploadFilesList;
	}
}
