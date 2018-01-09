package main.service;

import java.util.Date;
import java.util.List;
import main.model.Order;
import main.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

	Order get(Long id);

	@Deprecated
	List<Order> getAll();

	List<Order> getAllAllowed(User user);

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

	List<Object> statisticGeo(Date start, Date end);

	List<Object> statisticNewCustomers(Date start, Date end);

	List<Order> sorting(List<Order> list, String sortBy);

	Page<Order> getOrdersForDashboard(User user, Date start, Date end, String search, Double min, Double max,
		Pageable pageable);

	Page<Order> getOrdersForDashboardBoss(Date start, Date end, String search, Double min, Double max,
		Pageable pageable);

}
