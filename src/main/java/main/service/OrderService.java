package main.service;

import main.model.Order;

import java.util.List;

public interface OrderService {
     Order get(Long id);
     List<Order> getAll();
     List<Order> findByCustomer (String name);
     List<Order> findByNumber (String number);
     List<Order> findByManager (String name);
}
