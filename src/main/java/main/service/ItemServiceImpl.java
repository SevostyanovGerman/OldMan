package main.service;

import main.model.Item;
import main.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private ItemRepository itemRepository;

	@Override
	public Item get(Long id) {
		return itemRepository.getOne(id);
	}

	@Override
	public void save(Item item) {
		itemRepository.save(item);
	}
}
