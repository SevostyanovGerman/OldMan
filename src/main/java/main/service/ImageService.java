package main.service;

import main.model.Image;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.List;

public interface ImageService {

	void save(Image image);
	void delete(Image image);
	Image get(Long id);
	void saveBlobImage(List<MultipartFile> files, Long itemId);
	List<Image> uploadAndSaveBlobFile(MultipartHttpServletRequest uploadFiles);
}
