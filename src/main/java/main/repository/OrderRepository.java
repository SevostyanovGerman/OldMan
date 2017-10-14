package main.repository;

import main.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(Long id);
    List<Order> findAllByDeleted(int deleted);
    List<Order> findAllByDeletedAndNumberContains(int deleted, String number);
}
