package main.service;

import main.model.File;
import main.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
public class FileServiceImpl implements FileService{

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private FileService fileService;

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
	public List<File> uploadAndSaveBlobFile(MultipartHttpServletRequest uploadFiles) {
		List<File> blobFilesList = new ArrayList<>();
		List<MultipartFile> fileList = uploadFiles.getFiles("uploadCustomerFiles");
		for (MultipartFile file : fileList) {
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
}
