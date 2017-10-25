package main.controller;

import main.model.Customer;
import main.model.Delivery;
import main.model.Order;
import main.service.CustomerService;
import main.service.DeliveryService;
import main.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ManagerController {

	private OrderService orderService;

	private DeliveryService deliveryService;

	private CustomerService customerService;

	private final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@Autowired
	public ManagerController(OrderService orderService, DeliveryService deliveryService,
							 CustomerService customerService) {
		this.orderService = orderService;
		this.deliveryService = deliveryService;
		this.customerService = customerService;
	}

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

	@RequestMapping(value = {"/manager/order/addcustomer/{id}"}, method = RequestMethod.POST)
	public ModelAndView addCustomer(@PathVariable("id") Long id, HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		Order order = orderService.get(id);
		model.addObject("order", order);
		try {
			String firsName = request.getParameter("firstNameField");
			String secName = request.getParameter("secNameField");
			String email = request.getParameter("emailField");
			String phone = request.getParameter("phoneField");
			String country = request.getParameter("countryField");
			String city = request.getParameter("cityField");
			String address = request.getParameter("addressField");
			String zip = request.getParameter("zipField");
			Delivery delivery = new Delivery(country, city, address, zip);
			deliveryService.save(delivery);
			Customer customer = new Customer(firsName, secName, email, phone, delivery);
			customerService.save(customer);
			order.setCustomer(customer);
			orderService.save(order);
		} catch (DataIntegrityViolationException e) {
			order = orderService.get(id);
			model.addObject("order", order);
			model.addObject("error", "Пользователь существует");
		} catch (Exception e) {
			model = new ModelAndView("/managerView/ManagerDashBoard");
		}
		return model;
	}
}
