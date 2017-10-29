package main.service;

import main.model.Order;
import main.model.Status;
import main.model.User;
import main.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private StatusService statusService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private HistoryService historyService;

	private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Override
	public Order get(Long id) {
		logger.debug("Searching role with id: {}", id);
		return orderRepository.findByIdAndDeleted(id, 0);
	}

	@Override
	@Deprecated
	public List <Order> getAll() {
		return orderRepository.findAllByDeleted(0);
	}

	@Override
	public List <Order> getAllAllowed(User user) {
		Set <Status> statusSet = user.getStatuses();
		List <Order> list = new ArrayList <>();
		for (Status status : statusSet) {
			list.addAll(orderRepository.findAllByStatusAndDeleted(status, 0));
		}
		return list;
	}

	@Override
	public List <Order> designerOrders() {
		return orderRepository.findAllByDeletedAndStatusId(0, 1l);
	}

	@Override
	public List <Order> designFindNumber(String number) {
		return orderRepository.findAllByDeletedAndStatusIdAndNumberContains(0, 1l, number);
	}

	@Override
	public void save(Order order) {
		orderRepository.saveAndFlush(order);
	}

	@Override
	public Order nextStatus(Long orderId) {
		Date date = new Date();
		Order order = orderService.get(orderId);
		Long currentStatus = order.getStatus().getNumber();
		Status nextStatus = statusService.get(currentStatus + 1l);
		order.setStatus(nextStatus);
		order = historyService.saveHistory(order);
		order.setDateRecieved(order.getDateTransferredDate());
		order.setDateTransferred(date);
		order = setAllStatusItemFalse(order);
		orderService.save(order);
		return order;
	}

	@Override
	public Order previousStatus(Long orderId) {
		Date date = new Date();
		Order order = orderService.get(orderId);
		Long currentStatus = order.getStatus().getNumber();
		if (currentStatus > 1) {
			Status nextStatus = statusService.get(currentStatus - 1l);
			order.setStatus(nextStatus);
		}
		order = historyService.saveHistory(order);
		order.setDateRecieved(order.getDateTransferredDate());
		order.setDateTransferred(date);
		order = setAllStatusItemFalse(order);
		orderService.save(order);
		return order;
	}

	@Override
	public List <Order> findByCustomer(String name) {
		return orderRepository.findAllByCustomerFirstNameContainsAndDeleted(name, 0);
	}

	@Override
	public List <Order> findByNumber(String number) {
		return orderRepository.findAllByDeletedAndNumberContains(0, number);
	}

	@Override
	public List <Order> findByManager(String name) {
		return orderRepository.findAllByDeletedAndManagerFirstNameContains(0, name);
	}

	@Override
	public List <Order> searchByAllFields(String searchTerm) {
		return orderRepository.findBySearchTerm(searchTerm);
	}

	@Override
	public Order getPayment(Long orderId) {
		Order order = orderService.get(orderId);
		if (order.getPayment()) {
			order.setPayment(false);
		} else {
			order.setPayment(true);
		}
		return order;
	}

	@Override
	public Order changeStatusTo(Long orderId, Long statusId) {
		Date date = new Date();
		Order order = orderService.get(orderId);
		Status newStatus = statusService.get(statusId);
		order.setStatus(newStatus);
		order = historyService.saveHistory(order);
		order.setDateRecieved(order.getDateTransferredDate());
		order.setDateTransferred(date);
		order = setAllStatusItemFalse(order);
		orderService.save(order);
		return order;
	}

	@Override
	public Order setAllStatusItemFalse(Order order) {
		for (int i = 0; i < order.getItems().size(); i++) {
			order.getItems().get(i).setStatus(false);
		}
		return order;
	}
}
