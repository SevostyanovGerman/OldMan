package main.service.serviceImpl;

import main.model.Order;
import main.repository.OrderRepository;
import main.service.HistoryService;
import main.service.OrderService;
import main.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StatusService statusService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private HistoryService historyService;

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
        return orderRepository.findAllByDeletedAndStatusIdAndNumberContains(0,1l, number);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order changeStatus(Long orderId, Long newStatus) {
        Date date = new Date();
        Order order = orderService.get(orderId);
        order.setStatus(statusService.get(newStatus));
        order = historyService.saveHistory(order);
        order.setDateRecieved(order.getDateTransferredDate());
        order.setDateTransferred(date);
        orderService.save(order);
        return order;
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

    public List<Order> searchByAllFields(String searchTerm) {
        return orderRepository.findBySearchTerm(searchTerm);
    }
}
