package main.service;

import main.model.History;
import main.model.Order;
import main.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {
	@Autowired
	private HistoryRepository historyRepository;

	@Override
	public Order saveHistory(Order order) {
		Date dateTime = new Date();

		History history = new History();
		history.setFrom(order.getTo());
		history.setTo(order.getManager().toString());
		history.setDateRecieved(order.getDateRecievedDate());
		history.setDateTransferred(dateTime);
		history.setStatus(order.getStatus().getName());
		historyRepository.saveAndFlush(history);
		order.getHistories().add(history);
		return order;
	}
}
