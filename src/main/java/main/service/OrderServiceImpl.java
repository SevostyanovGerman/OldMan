package main.service;

import main.model.Order;
import main.model.Role;
import main.model.Status;
import main.model.User;
import main.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

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
		return orderRepository.findByIdAndDeleted(id, false);
	}

	@Override
	public List<Order> getAll() {
		return orderRepository.findAllByDeleted(false);
	}

	@Override
	public List<Order> getAllAllowed(User user) {
		Set<Role> roleSet = user.getRoles();
		Set<Order> statusSet = new HashSet<>();
		for (Role role : roleSet) {
			Set<Status> tmpStatusSet = role.getStatuses();
			for (Status status : tmpStatusSet) {
				statusSet.addAll(orderRepository.findByUser(user, status, false));
			}
		}
		return new ArrayList<>(statusSet);
	}

	@Override
	public List<Order> designerOrders() {
		return orderRepository.findAllByDeletedAndStatusId(false, 1l);
	}

	@Override
	public List<Order> designFindNumber(String number) {
		return orderRepository.findAllByDeletedAndStatusIdAndNumberContains(false, 1l, number);
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
		Status nextStatus = statusService.get(currentStatus + 1L);
		order.setStatus(nextStatus);
		order = historyService.saveHistoryToManager(order);
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
		order = historyService.saveHistoryToManager(order);
		order.setDateRecieved(order.getDateTransferredDate());
		order.setDateTransferred(date);
		order = setAllStatusItemFalse(order);
		orderService.save(order);
		return order;
	}

	@Override
	public List<Order> findByCustomer(String name) {
		return orderRepository.findAllByCustomerFirstNameContainsAndDeleted(name, false);
	}

	@Override
	public List<Order> findByNumber(String number) {
		return orderRepository.findAllByDeletedAndNumberContains(false, number);
	}

	@Override
	public List<Order> findByManager(String name) {
		return orderRepository.findAllByDeletedAndManagerFirstNameContains(false, name);
	}

	@Override
	public List<Order> searchByAllFields(String searchTerm) {
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
		order = historyService.saveHistoryFromManager(order);
		order.setDateRecieved(order.getDateTransferredDate());
		order.setDateTransferred(date);
		order = setAllStatusItemFalse(order);
		orderService.save(order);
		return order;
	}

	public Order setAllStatusItemFalse(Order order) {
		for (int i = 0; i < order.getItems().size(); i++) {
			order.getItems().get(i).setStatus(false);
		}
		return order;
	}

	@Override
	public List<Object> avgPriceByMonth(Date start, Date end) {
		return orderRepository.priceAvgByMonth(start, end);
	}

	@Override
	public List<Object> statisticGeo() {
		return orderRepository.statisticGeo();
	}

	@Override
	public List<Object> statisticNewCustomers(Date start, Date end) {
		return orderRepository.statisticNewCustomers(start, end);
	}

	@Override
	public List<Order> minMaxPrice(Double min, Double max, User user) {
		Set<Role> roleSet = user.getRoles();
		Set<Order> statusSet = new HashSet<>();
		for (Role role : roleSet) {
			Set<Status> tmpStatusSet = role.getStatuses();
			if (role.getName().equals("BOSS")) {
				statusSet.addAll(orderRepository.minMaxPriceForBoss(min, max));
				return new ArrayList<>(statusSet);
			}
			for (Status status : tmpStatusSet) {
				statusSet.addAll(orderRepository.minMaxPrice(min, max, user, status));
			}
		}
		return new ArrayList<>(statusSet);
	}

	@Override
	public void deleteOrder(Order order) {
		order.setDeleted(true);
	}

	@Override
	public List<Order> findOrdersByRange(Date startDate, Date endDate) {
		return orderRepository.findOrdersByRange(startDate, endDate);
	}
}
