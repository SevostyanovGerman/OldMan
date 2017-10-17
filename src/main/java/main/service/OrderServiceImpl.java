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
        return orderRepository.findByIdAndDeleted(id,0);
    }

    @Override
    public List <Order> getAll() {
        return orderRepository.findAllByDeleted(0);
    }

    @Override
    public List <Order> designerOrders() {
        return orderRepository.findAllByDeletedAndStatusId(0,1l);
    }

    @Override
    public List <Order> designFindNumber(String number) {
        return orderRepository.findAllByDeletedAndStatusAndNumberContains(0,"design", number);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
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
