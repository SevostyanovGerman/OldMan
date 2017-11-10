package main.service;

import main.model.File;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface FileService {

	void save(File file);
	void delete(File file);
	File get(Long id);
	boolean saveBlobFile(MultipartHttpServletRequest multipartRequest, Long itemId);
}
