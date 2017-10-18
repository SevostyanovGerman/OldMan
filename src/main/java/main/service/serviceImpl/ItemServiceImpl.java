package main.service.serviceImpl;

import main.model.Item;
import main.repository.ItemRepository;
import main.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
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
