package main.service;

import main.model.Order;
import main.model.Role;
import main.model.Status;
import main.model.User;
import main.repository.OrderRepository;
import org.joda.time.DateTime;
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

	@Autowired
	private RoleService roleService;

	private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

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
	public List<Order> getAllAllowedByDate(User user, Date start, Date end) {

		Set<Role> roleSet = user.getRoles();
		Set<Order> statusSet = new HashSet<>();
		if (start == null) {
			start = new DateTime().dayOfMonth().withMinimumValue().toDate();
		}
		if (end == null) {
			Date today = new Date();
			end = new DateTime(today).withHourOfDay(23).withMinuteOfHour(59).toDate();
		}
		for (Role role : roleSet) {
			if (role.getName().equals("BOSS")) {
				return orderRepository.findByUserAndDateBoss(false, start, end);
			}
			Set<Status> tmpStatusSet = role.getStatuses();
			for (Status status : tmpStatusSet) {
				statusSet
					.addAll(orderRepository.findByUserAndDate(user, status, false, start, end));
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
	public List<Order> searchByAllFields(String searchTerm, Date start, Date end) {
		return orderRepository.findBySearchTerm(searchTerm, start, end);
	}

	@Override
	public Order getPayment(Long orderId) {
		Order order = orderService.get(orderId);
		order.setPayment(!order.getPayment());
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
	public List<Object> avgPriceByMonth(Date start, Date end, String dwm) {
		return orderRepository.priceAvgByMonth(start, end, dwm);
	}

	@Override
	public List<Object> sumPriceOrders(Date start, Date end, String dwm) {
		return orderRepository.sumPriceOrders(start, end, dwm);
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
	public List<Order> filterByPrice(Double min, Double max, User user, Date start, Date end) {
		Set<Role> roleSet = user.getRoles();
		Set<Order> statusSet = new HashSet<>();
		for (Role role : roleSet) {
			Set<Status> tmpStatusSet = role.getStatuses();
			if (role.getName().equals("BOSS")) {
				statusSet.addAll(orderRepository.filterByPriceForBoss(min, max, start, end));
				return new ArrayList<>(statusSet);
			}
			for (Status status : tmpStatusSet) {
				statusSet.addAll(orderRepository.filterByPrice(min, max, user, status, start, end));
			}
		}
		return new ArrayList<>(statusSet);
	}

	@Override
	public List<Order> filterByPriceMin(Double min, User user, Date start, Date end) {
		Set<Role> roleSet = user.getRoles();
		Set<Order> statusSet = new HashSet<>();
		for (Role role : roleSet) {
			Set<Status> tmpStatusSet = role.getStatuses();
			if (role.getName().equals("BOSS")) {
				statusSet.addAll(orderRepository.filterByPriceMinBoss(min, start, end));
				return new ArrayList<>(statusSet);
			}
			for (Status status : tmpStatusSet) {
				statusSet.addAll(orderRepository.filterByPriceMin(min, user, status, start, end));
			}
		}
		return new ArrayList<>(statusSet);
	}

	@Override
	public List<Order> filterByPriceMax(Double max, User user, Date start, Date end) {
		Set<Role> roleSet = user.getRoles();
		Set<Order> statusSet = new HashSet<>();
		for (Role role : roleSet) {
			Set<Status> tmpStatusSet = role.getStatuses();
			if (role.getName().equals("BOSS")) {
				statusSet.addAll(orderRepository.filterByPriceMaxBoss(max, start, end));
				return new ArrayList<>(statusSet);
			}
			for (Status status : tmpStatusSet) {
				statusSet.addAll(orderRepository.filterByPriceMax(max, user, status, start, end));
			}
		}
		return new ArrayList<>(statusSet);
	}

	@Override
	public List<Order> sorting(List<Order> list, String sortBy) {

		switch (sortBy) {
			case "name":
				Collections.sort(list, Order.nameComparator);
				return list;
			case "price":
				Collections.sort(list, Order.priceComparator);
				return list;
			case "number":
				Collections.sort(list, Order.numberComparator);
				return list;
			case "status":
				Collections.sort(list, Order.statusComparator);
				return list;
		}
		return null;
	}

	@Override
	public void deleteOrder(Order order) {
		order.setDeleted(true);
	}

	@Override
	public List<Order> findOrdersByRange(Date startDate, Date endDate) {
		return orderRepository.findOrdersByRange(startDate, endDate);
	}

	@Override
	public List<Order> getOrdersForDashboard(User user, Date start, Date end, String search,
											 Double min, Double max) {

		Set<Role> roleSet = user.getRoles();
		for (Role role : roleSet) {
			if (role.getName().equals("BOSS")) {
				return orderRepository.filterForDashboardBoss(search, min, max, start, end);
			}

		}
		return orderRepository.filterForDashboard(user, search, min, max, start, end);
//
//
//		if (search.equals("") && max == null && min == null) {
//			return getAllAllowedByDate(user, start, end);
//		}
//
//		if (search.equals("")) {
//
//			if (max == null) {
//				return filterByPriceMin(min, user, start, end);
//			}
//			if (min == null) {
//				return filterByPriceMax(max, user, start, end);
//			}
//
//			return filterByPrice(min, max, user, start, end);
//		}
//
//		if (min == null && max == null) {
//			return searchByAllFields(search, start, end);
//		}
//
//		if (max == null) {
//			return orderRepository.findBySearchTermAndMin(search, start, end, min);
//		}
//		if (min == null) {
//			return orderRepository.findBySearchTermAndMax(search, start, end, max);
//		}
//		return orderRepository.findBySearchTermAndMinMax(search, start, end, min, max);
	}
}

