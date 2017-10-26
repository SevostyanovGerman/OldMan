package main.service;

import main.model.Image;

public interface ImageService {

	void save(Image image);
	void del(Image image);
	Image get(Long id);
}
