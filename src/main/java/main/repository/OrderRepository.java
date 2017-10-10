package main.repository;

import main.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(Long id);
    List<Order> findAllByDeleted(int deleted);
}
