package main.controller;

import main.model.*;
import main.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class ManagerController {

	private OrderService orderService;

	private DeliveryService deliveryService;

	private CustomerService customerService;

	private ItemService itemService;

	private UserService userService;

	private StatusService statusService;

	private PaymentService paymentService;

	private final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@Autowired
	public ManagerController(OrderService orderService, DeliveryService deliveryService,
							 CustomerService customerService, ItemService itemService,
							 UserService userService, StatusService statusService,
							 PaymentService paymentService) {
		this.orderService = orderService;
		this.deliveryService = deliveryService;
		this.customerService = customerService;
		this.itemService = itemService;
		this.userService = userService;
		this.statusService = statusService;
		this.paymentService = paymentService;
	}

	@RequestMapping(value = {"/manager"}, method = RequestMethod.GET)
	public ModelAndView getOrderList() {
		ModelAndView model = new ModelAndView("/managerView/ManagerDashBoard");
		User authUser = userService.getCurrentUser();
		List <Order> orderList = orderService.getAllAllowed(authUser);
		model.addObject("authUser", authUser);
		model.addObject("orderList", orderList);
		return model;
	}

	@RequestMapping(value = {"/manager/order/update/{id}"}, method = RequestMethod.GET)
	public ModelAndView updateOrder(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		model.addObject("authUser", userService.getCurrentUser());
		model.addObject("order", orderService.get(id));
		return model;
	}

	@RequestMapping(value = {"/manager/order/send/{id}"}, method = RequestMethod.GET)
	public ModelAndView sentOrder(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		model.addObject("order", orderService.nextStatus(id));
		return new ModelAndView("redirect:/manager");
	}

	//Добавляем новый заказ с новой позицией
	@RequestMapping(value = {"/manager/order/add"}, method = RequestMethod.GET)
	public ModelAndView addItem() {
		ModelAndView model = new ModelAndView("/managerView/ManagerNewOrder");
		Order order = new Order();
		Item item = new Item();
		model.addObject("authUser", userService.getCurrentUser());
		model.addObject("order", order);
		model.addObject("item", item);
		return model;
	}

	//Сохраняем новый заказ с новой позицией
	@RequestMapping(value = {"/manager/item/saveNewOrder"}, method = RequestMethod.GET)
	public ModelAndView save(@ModelAttribute("order") Order order, @ModelAttribute("item") Item item){
		order.setCreated(new Date());
		order.setPayment(false);
		order.setStatus(statusService.getByName("new"));
		order.setManager(userService.getCurrentUser());
		order.setPaymentType(paymentService.getByName("Cash"));
		item.setStatus(false);
		orderService.save(order);
		item.setOrder(order);
		itemService.save(item);
		Long orderId = order.getId();
		return new ModelAndView("redirect:/manager/order/update/"+orderId);
	}

	//Добавляем новую позицию в существующий заказ
	@RequestMapping(value = {"/manager/order/addItem/{orderId}"}, method = RequestMethod.GET)
	public ModelAndView addItem(@PathVariable("orderId") Long orderId) {
		ModelAndView model = new ModelAndView("/managerView/ManagerItemForm");
		Item item = new Item();
		model.addObject("authUser", userService.getCurrentUser());
		model.addObject("order", orderService.get(orderId));
		model.addObject("item", item);
		return model;
	}

	//Обновляем существующую позицию в существующем заказе
	@RequestMapping(value = {"/manager/item/update/{orderId}/{itemId}"}, method = RequestMethod.GET)
	public ModelAndView updateItem(@PathVariable("orderId") Long orderId, @PathVariable("itemId") Long itemId){
		ModelAndView model = new ModelAndView("/managerView/ManagerItemForm");
		model.addObject("authUser", userService.getCurrentUser());
		model.addObject("order", orderService.get(orderId));
		model.addObject("item", itemService.get(itemId));
		return model;
	}

	//Сохраняем позицию (новую или обновлённую) в существующем заказе
	@RequestMapping(value = {"/manager/item/save/{orderId}"}, method = RequestMethod.GET)
	public ModelAndView save(@PathVariable("orderId") String orderId, @ModelAttribute("item") Item item){
		Order order = orderService.get(Long.parseLong(orderId));
		item.setOrder(order);
		itemService.save(item);
		Long redirectOrderId = order.getId();
		return new ModelAndView("redirect:/manager/order/update/"+redirectOrderId);
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
