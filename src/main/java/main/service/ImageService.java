package main.service;

import main.model.Image;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface ImageService {

	void save(Image image);
	void del(Image image);
	Image get(Long id);
	public boolean saveBlobImage(MultipartHttpServletRequest multipartRequest, Long itemId);
}
