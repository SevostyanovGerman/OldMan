package main.controller.manager;


import main.model.Order;
import main.model.User;
import main.service.OrderService;
import main.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerDashController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(ManagerDashController.class);

    @RequestMapping(value = {"/orderlist"}, method = RequestMethod.GET)
    public ModelAndView getOrderList() {
        ModelAndView model = new ModelAndView("/managerView/ManagerDashBoard");
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByName(name);
        List<Order> orderList = orderService.getAllAllowed(user);
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
}
