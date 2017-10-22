package main.service;

import main.model.Order;
import main.model.User;

import java.util.List;

public interface OrderService {
     Order get(Long id);
     List<Order> getAll();
     List<Order> getAllAllowed(User user);
     List<Order> findByCustomer (String name);
     List<Order> findByNumber (String number);
     List<Order> findByManager (String name);
     List<Order> designerOrders();
     List<Order> designFindNumber (String number);
     void save(Order order);
     Order changeStatus (Long orderId, Long newStatus);
     List<Order> searchByAllFields(String searchTerm);
}
