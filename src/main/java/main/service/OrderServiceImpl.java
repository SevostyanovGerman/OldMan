package main.service;

import main.model.Order;
import main.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public Order get(Long id) {
        logger.debug("Searching role with id: {}", id);
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getAll() {
        logger.debug("Getting order list");
        return orderRepository.findAllByDeleted(0);
    }

    @Override
    public List <Order> findByCustomer(String name) {
        return orderRepository.findAllByCustomerFirstNameContainsAndDeleted(name,0);
    }

    @Override
    public List <Order> findByNumber(String number) {
        return orderRepository.findAllByDeletedAndNumberContains(0, number);
    }

    @Override
    public List <Order> findByManager(String name) {
        return orderRepository.findAllByDeletedAndManagerFirstNameContains(0, name);
    }
}
