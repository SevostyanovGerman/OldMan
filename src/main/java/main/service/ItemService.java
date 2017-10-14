package main.service;

import main.model.Item;

public interface ItemService {

	Item get(Long id);
	void save(Item item);
}
