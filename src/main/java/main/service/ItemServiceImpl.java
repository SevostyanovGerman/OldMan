package main.service;

import main.model.Item;
import main.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemService itemService;

	@Override
	public Item get(Long id) {
		return itemRepository.getOne(id);
	}

	@Override
	public void save(Item item) {
		itemRepository.save(item);
	}

	@Override
	public Item changeStatus(Long itemId) {
		Item item = itemService.get(itemId);
		if (item.getStatus()) {
			item.setStatus(false);
		} else {
			item.setStatus(true);
		}
		return item;
	}

	@Override
	public void delete(Long id) {
		itemRepository.delete(id);
	}
}
