package main.service;

import main.model.Item;

public interface ItemService {

	Item get(Long id);

	void save(Item item);

	Item changeStatus(Long itemId);

	void delete(Long id);
}
