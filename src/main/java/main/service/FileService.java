package main.service;

import main.model.File;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface FileService {

	void save(File file);
	void delete(File file);
	File get(Long id);
	List<File> saveBlobFile(MultipartHttpServletRequest multipartRequest);
	List<File> uploadFile(MultipartFile[] uploadCustomerFiles) throws SQLException, IOException;
}
