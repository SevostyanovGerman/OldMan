package main.controller;


import main.model.Order;
import main.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @RequestMapping(value = {"/orderlist"}, method = RequestMethod.GET)
    public ModelAndView getOrderList() {
        ModelAndView model = new ModelAndView("/managerView/ManagerDashBoard");
        List<Order> orderList = orderService.getAll();
        model.addObject("orderList", orderList);
        return model;
    }

    @RequestMapping(value = {"/updateorder/{id}"}, method = RequestMethod.GET)
    public ModelAndView updateOrder(@PathVariable("id") Long id) {
        ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
        Order order = orderService.get(id);
        model.addObject("order", order);
        return model;
    }

    @RequestMapping(value = {"/addorder"}, method = RequestMethod.GET)
    public ModelAndView addOrder() {
        ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
        Order order = new Order();
        model.addObject("order", order);
        return model;
    }

    @RequestMapping(value = {"/send/order={id}&status={statusId}"}, method = RequestMethod.POST)
    public ModelAndView sentOrder(@PathVariable Long id,@PathVariable Long statusId){
        ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
        model.addObject("order", orderService.changeStatus(id, statusId));
        return new ModelAndView("/managerView/ManagerDushBoard");
    }
}
