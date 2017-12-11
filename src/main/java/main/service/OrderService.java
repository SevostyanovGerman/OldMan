package main.service;

import main.model.Order;
import main.model.User;
import java.util.Date;
import java.util.List;

public interface OrderService {

	Order get(Long id);

	@Deprecated
	List<Order> getAll();

	List<Order> getAllAllowed(User user);
	List<Order> getAllAllowedByDate(User user, Date start, Date end);
	List<Order> findByCustomer(String name);
	List<Order> findByNumber(String number);
	List<Order> findByManager(String name);
	List<Order> designerOrders();
	List<Order> designFindNumber(String number);
	void save(Order order);
	Order nextStatus(Long orderId);
	Order previousStatus(Long orderId);
	List<Order> searchByAllFields(String searchTerm, Date start, Date end);
	Order getPayment(Long orderId);
	Order changeStatusTo(Long orderId, Long statusId);  //изменяем статус заказа на выбранный
	void deleteOrder(Order order);//Change value "delete" from false to true. Order do not delete!
	List<Order> findOrdersByRange(Date startDate, Date endDate);
	Order setAllStatusItemFalse(Order order);
	List<Object> avgPriceByMonth(Date start, Date end, String dwm);
	List<Object> sumPriceOrders(Date start, Date end, String dwm);
	List<Object> statisticGeo();
	List<Object> statisticNewCustomers(Date start, Date end);
	List<Order> filterByPrice(Double min, Double max, User user, Date start, Date end);
	List<Order> filterByPriceMin(Double min, User user, Date start, Date end);
	List<Order> filterByPriceMax(Double max, User user, Date start, Date end);
	List<Order> sorting(List<Order> list, String sortBy);
	List<Order> getOrdersForDashboard(User user, Date start, Date end, String search, Double min,
									  Double max);
}
