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

	private HistoryRepository historyRepository;

	private UserService userService;

	private RoleService roleService;

	@Autowired
	public HistoryServiceImpl(HistoryRepository historyRepository, UserService userService, RoleService roleService) {
		this.historyRepository = historyRepository;
		this.userService = userService;
		this.roleService = roleService;
	}

	@Override
	public Order saveHistoryFromManager(Order order) {
		Date dateTime = new Date();
		String to;
		if (order.getDesigner().getStatuses().contains(order.getStatus())) {
			to = order.getDesigner().toString();
		} else {
			to = order.getMaster().toString();
		}
		History history = new History(order.getDateRecievedDate(), dateTime, order.getStatus().getName(),
			userService.getCurrentUser().toString(), to);
		historyRepository.saveAndFlush(history);
		order.getHistories().add(history);
		return order;
	}

	@Override
	public Order saveHistoryToManager(Order order) {
		Date dateTime = new Date();
		String to = order.getManager().toString();
		History history = new History(order.getDateRecievedDate(), dateTime, order.getStatus().getName(),
			userService.getCurrentUser().toString(), to);
		historyRepository.saveAndFlush(history);
		order.getHistories().add(history);
		return order;
	}
}
