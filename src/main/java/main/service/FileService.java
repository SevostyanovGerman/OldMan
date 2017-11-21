package main.service;

import main.model.File;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.List;

public interface FileService {

	void save(File file);
	void delete(File file);
	File get(Long id);
	List<File> uploadAndSaveBlobFile(MultipartHttpServletRequest uploadFiles);
}
