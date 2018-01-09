package main.service;

import javax.transaction.Transactional;
import main.model.Item;
import main.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

	private ItemRepository itemRepository;

	@Autowired
	public ItemServiceImpl(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@Override
	public Item get(Long id) {
		return itemRepository.getOne(id);
	}

	@Override
	public void save(Item item) {
		itemRepository.saveAndFlush(item);
	}

	@Override
	public Item changeStatus(Long itemId) {
		Item item = get(itemId);
		item.setStatus(!item.getStatus());
		return item;
	}

	@Override
	public void delete(Long id) {
		itemRepository.delete(id);
	}
}
