package main.controller;

import main.model.Item;
import main.model.Order;
import main.service.ItemService;
import main.service.OrderService;
import main.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.logging.LogManager;

@Controller
public class MasterController {

	private final Logger logger = LoggerFactory.getLogger(DesignerController.class);
	private OrderService orderService;
	private ItemService itemService;
	private UserService userService;

	@Autowired
	public MasterController(OrderService orderService, ItemService itemService, UserService userService) {
		this.orderService = orderService;
		this.itemService = itemService;
		this.userService = userService;
	}

	@RequestMapping(value = {"/master"}, method = RequestMethod.GET)
	public String getMasterDashBoard(Model model) {
		try {
			List <Order> masterOrders = orderService.getAllAllowed(userService.getCurrentUser());
			model.addAttribute("masterOrders", masterOrders);
		} catch (Exception e) {
			logger.error("Controller '/master' orderService.masterOrders() error");
		}

		return "masterView/MasterDashBoard";
	}

	@RequestMapping(value = {"/master/order/{id}"}, method = RequestMethod.GET)
	public String getOrderForm(@PathVariable("id") Long id, Model model) {
		try {
			Order order = orderService.get(id);
			model.addAttribute("order", order);
		} catch (Exception e) {
			logger.error("Controller '/master/order/', orderId={}", id);
		}

		return "masterView/MasterOrderForm";
	}

	@RequestMapping(value = {"/master/order/item/{id}"}, method = RequestMethod.GET)
	public String getItemForm(@PathVariable("id") Long id, Model model) {
		try {
			Item item = itemService.get(id);
			model.addAttribute(item);
		} catch (Exception e) {
			logger.error("Controller '/master/order/item/', itemId={}", id);
		}
		return "masterView/MasterItemForm";
	}

	@RequestMapping(value = {"/master/search"}, method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("masterView/MasterDashBoard");
		try {
			String searchItem = request.getParameter("searchItem");
			model.addObject("masterOrders", orderService.searchByAllFields(searchItem));
		} catch (Exception e) {
			logger.error("Controller '/master/search' doesn't work ");
			model = new ModelAndView("masterView/MasterDashBoard");
		}

		return model;
	}

	@RequestMapping(value = {"/master/order/{id}/send"}, method = RequestMethod.GET)
	public ModelAndView changeStatus(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("masterView/MasterOrderForm");
		try {
			model.addObject("order", orderService.nextStatus(id));
		} catch (Exception e) {
			logger.warn("You couldn't change the status of order");
			model = new ModelAndView("masterView/MasterOrderForm");
		}
		return model;
	}

	@RequestMapping(value = {"/master/order/{id}/money"}, method = RequestMethod.GET)
	public ModelAndView getPayment(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("masterView/MasterOrderForm");
		try {
			model.addObject("order", orderService.getPayment(id));
		} catch (Exception e) {
			logger.warn("You couldn't get the money for order");
			model = new ModelAndView("masterView/MasterOrderForm");
		}
		return model;
	}

	@RequestMapping(value = {"/master/order/item/{id}/status"}, method = RequestMethod.GET)
	public ModelAndView changeItemStatus(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("masterView/MasterItemForm");
		try {
			Item newItem = itemService.changeStatus(id);
			itemService.save(newItem);
			model.addObject("item", newItem);
		} catch (Exception e) {
			logger.warn("You couldn't change the status of item");
			new ModelAndView("masterView/MasterItemForm");
		}
		return model;
	}

	@RequestMapping(value = {"master/ordersByRange"}, method = RequestMethod.POST)
	public ModelAndView getOrdersByRange(Date startDate, Date endDate) {
		ModelAndView model = new ModelAndView("masterView/MasterDashBoard");
		List <Order> rangeOrders = orderService.findOrdersByRange(startDate, endDate);
		model.addObject("masterOrders", rangeOrders);
		return model;
	}
}