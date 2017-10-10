package main.service;

import main.model.Order;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService {
     Order get(Long id);
     List<Order> getAll();
}
