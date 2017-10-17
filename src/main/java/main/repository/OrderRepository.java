package main.repository;

import main.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndDeleted(Long id, int deleted);
    List<Order> findAllByDeleted(int deleted);
    List<Order> findAllByCustomerFirstNameContainsAndDeleted(String name, int deleted);
    List<Order> findAllByDeletedAndNumberContains(int deleted, String number);
    List<Order> findAllByDeletedAndManagerFirstNameContains(int deleted, String name);
    List<Order> findAllByDeletedAndStatusId(int deleted, Long id);
    List<Order> findAllByDeletedAndStatusAndNumberContains(int deleted, String status, String number);
}
