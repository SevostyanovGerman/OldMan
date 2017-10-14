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
    public List <Order> designerOrders() {
        return orderRepository.findAllByDeletedAndStatus(0,"design");
    }

    @Override
    public List <Order> designFindNumber(String number) {
        return orderRepository.findAllByDeletedAndStatusAndNumberContains(0,"design", number);
    }
}
