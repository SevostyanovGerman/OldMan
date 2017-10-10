package main.service;

import main.model.Order;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService {
    public Order get(Long id);
    public List<Order> getAll();
}
