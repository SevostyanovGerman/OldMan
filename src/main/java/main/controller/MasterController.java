package main.controller;

import main.model.Item;
import main.model.Order;
import main.service.ItemService;
import main.service.OrderService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MasterController {

    @Autowired
    OrderService orderService;

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;


    @RequestMapping(value = {"/master"}, method = RequestMethod.GET)
    public String getMasterDashBoard(Model model) {
        List<Order> allOrders = orderService.getAll();
          model.addAttribute("allOrders", allOrders);
        return "masterView/MasterDashBoard";
    }

    @RequestMapping(value = {"/master/order/{id}"}, method = RequestMethod.GET)
    public String getOrderForm(@PathVariable ("id") Long id, Model model) {
        Order order = orderService.get(id);
        List<Item> allItems = order.getItems();
        model.addAttribute("order", order);
        model.addAttribute("allItems", allItems);
        return "masterView/MasterOrderForm";
    }

    @RequestMapping(value = {"/master/item/{id}"}, method = RequestMethod.GET)
    public String getItemForm(@PathVariable ("id") Long id, Model model) {
        Item item = itemService.get(id);
        model.addAttribute(item);
        return "masterView/MasterItemForm";
    }

}