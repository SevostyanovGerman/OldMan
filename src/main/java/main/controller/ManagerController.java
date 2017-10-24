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
public class ManagerController {

	@Autowired
	private OrderService orderService;

	private final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@RequestMapping(value = {"/manager"}, method = RequestMethod.GET)
	public ModelAndView getOrderList() {
		ModelAndView model = new ModelAndView("/managerView/ManagerDashBoard");
		List <Order> orderList = orderService.getAll();
		model.addObject("orderList", orderList);
		return model;
	}

	@RequestMapping(value = {"/manager/order/update/{id}"}, method = RequestMethod.GET)
	public ModelAndView updateOrder(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		Order order = orderService.get(id);
		model.addObject("order", order);
		return model;
	}

	@RequestMapping(value = {"/manager/order/add"}, method = RequestMethod.GET)
	public ModelAndView addOrder() {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		Order order = new Order();
		model.addObject("order", order);
		return model;
	}

	@RequestMapping(value = {"/manager/order/send={id}&status={statusId}"}, method = RequestMethod.GET)
	public ModelAndView sentOrder(@PathVariable Long id, @PathVariable Long statusId) {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		model.addObject("order", orderService.nextStatus(id));
		return new ModelAndView("redirect:/manager");
	}
}
