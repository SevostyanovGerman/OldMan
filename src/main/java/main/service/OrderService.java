package main.service;

import main.model.Order;
import main.model.User;
import java.util.List;

public interface OrderService {

	Order get(Long id);
	@Deprecated
	List <Order> getAll();
	List <Order> getAllAllowed(User user);
	@Deprecated
	List <Order> findByCustomer(String name);
	@Deprecated
	List <Order> findByNumber(String number);
	@Deprecated
	List <Order> findByManager(String name);
	List <Order> designerOrders();
	List <Order> designFindNumber(String number);
	void save(Order order);
	Order nextStatus(Long orderId);
	Order previousStatus(Long orderId);
	List <Order> searchByAllFields(String searchTerm);
	Order getPayment(Long orderId);
	Order changeStatusTo(Long orderId, Long statusId);  //изменяем статус заказа на выбранный
	void deleteOrder(Order order);//Change value "delete" from false to true. Order do not delete!
	Order setAllStatusItemFalse(Order order);
}
