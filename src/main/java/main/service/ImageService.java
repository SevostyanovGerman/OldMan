package main.service;

import main.model.Image;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImageService {

	void save(Image image);
	void del(Image image);
	Image get(Long id);
	public boolean saveBlobImage(List<MultipartFile> files, Long itemId);
}
