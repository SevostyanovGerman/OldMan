package main.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import main.model.Image;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface ImageService {

	void save(Image image);

	void delete(Image image);

	Image get(Long id);

	List<Image> uploadAndSaveBlobFile(MultipartHttpServletRequest uploadFiles) throws IOException, SQLException;

	void downloadAllFiles(List<Image> downloadFiles) throws IOException, SQLException;

	void downloadOneFile(Image downloadFile) throws IOException, SQLException;
}
