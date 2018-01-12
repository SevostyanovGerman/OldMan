package main.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import main.model.Order;
import main.model.Role;
import main.model.Status;
import main.model.User;
import main.repository.OrderRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepository;

	private StatusService statusService;

	private HistoryService historyService;

	private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, StatusService statusService,
		HistoryService historyService) {
		this.orderRepository = orderRepository;
		this.statusService = statusService;
		this.historyService = historyService;
	}

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
		Order order = get(orderId);
		Long currentStatus = order.getStatus().getNumber();
		Status nextStatus = statusService.get(currentStatus + 1L);
		order.setStatus(nextStatus);
		order = historyService.saveHistoryToManager(order);
		order.setDateRecieved(order.getDateTransferredDate());
		order.setDateTransferred(date);
		order = setAllStatusItemFalse(order);
		save(order);
		return order;
	}

	@Override
	public Order previousStatus(Long orderId) {
		Date date = new Date();
		Order order = get(orderId);
		Long currentStatus = order.getStatus().getNumber();
		if (currentStatus > 1) {
			Status nextStatus = statusService.get(currentStatus - 1l);
			order.setStatus(nextStatus);
		}
		order = historyService.saveHistoryToManager(order);
		order.setDateRecieved(order.getDateTransferredDate());
		order.setDateTransferred(date);
		order = setAllStatusItemFalse(order);
		save(order);
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
		Order order = get(orderId);
		order.setPayment(!order.getPayment());
		return order;
	}

	@Override
	public Order changeStatusTo(Long orderId, Long statusId) {
		Date date = new Date();
		Order order = get(orderId);
		Status newStatus = statusService.get(statusId);
		order.setStatus(newStatus);
		order = historyService.saveHistoryFromManager(order);
		order.setDateRecieved(order.getDateTransferredDate());
		order.setDateTransferred(date);
		order = setAllStatusItemFalse(order);
		save(order);
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
	public List<Object> statisticGeo(Date start, Date end) {
		return orderRepository.statisticGeo(start, end);
	}

	@Override
	public List<Object> statisticNewCustomers(Date start, Date end) {
		return orderRepository.statisticNewCustomers(start, end);
	}

	@Override
	public List<Order> sorting(List<Order> list, String sortBy) {

		switch (sortBy) {
			case "name":
				list.sort(Comparator.comparing(a -> a.getCustomer().getFirstName()));
				return list;
			case "price":
				list.sort(Comparator.comparingInt(a -> (int) a.getPrice()));
				return list;
			case "number":
				list.sort(Comparator.comparingInt(a -> Integer.parseInt(a.getNumber())));
				return list;
			case "status":
				list.sort(Comparator.comparingInt(a -> (int) a.getStatus().getNumber()));
				return list;
		}
		return null;
	}

	@Override
	//Загрузка страницы для дашборда из сессии
	public Page<Order> getOrderBySession(HttpSession session, User user) {

		String search = (String) session.getAttribute("search");
		Integer pageNumber = (Integer) session.getAttribute("pageNumber");
		Integer pageSize = (Integer) session.getAttribute("pageSize");
		String sort = (String) session.getAttribute("sort");
		String orderBy = (String) session.getAttribute("orderBy");
		Double minPrice = (Double) session.getAttribute("minPrice");
		Double maxPrice = (Double) session.getAttribute("maxPrice");

		if (sort == null) {
			sort = "number";
		}
		if (pageNumber == null) {
			pageNumber = 0;
		}
		if (pageSize == null) {
			pageSize = 25;
		}

		DateTime startDate;
		if (session.getAttribute("startDate") != null) {
			startDate = new DateTime(session.getAttribute("startDate"));
		} else {
			startDate = new DateTime().withDayOfMonth(1);
		}

		DateTime endDate;
		if (session.getAttribute("endDate") != null) {
			endDate = new DateTime(session.getAttribute("endDate")).withHourOfDay(23).withMinuteOfHour(59);
		} else {
			endDate = new DateTime().withHourOfDay(23).withMinuteOfHour(59);
		}

		Sort.Direction orderByDirection = Sort.Direction.fromString(orderBy);

		Sort sorting = new Sort(orderByDirection, sort);

		if (user == null) {
			return getOrdersForDashboardBoss(startDate.toDate(), endDate.toDate(), search, minPrice, maxPrice,
				new PageRequest(pageNumber, pageSize, sorting));
		}

		return getOrdersForDashboard(user, startDate.toDate(), endDate.toDate(), search, minPrice, maxPrice,
			new PageRequest(pageNumber, pageSize, sorting));

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
	public Page<Order> getOrdersForDashboard(User user, Date start, Date end, String search, Double min, Double max,
		Pageable pageable) {

		return orderRepository.filterForDashboard(user, search, min, max, start, end, pageable);
	}

	@Override
	public Page<Order> getOrdersForDashboardBoss(Date start, Date end, String search, Double min, Double max,
		Pageable pageable) {

		return orderRepository.filterForDashboardBoss(search, min, max, start, end, pageable);

	}
}

