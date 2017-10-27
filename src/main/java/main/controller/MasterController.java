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
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.LogManager;

@Controller
public class MasterController {

	private OrderService orderService;
	private ItemService itemService;
	private UserService userService;

	@Autowired
	public MasterController(OrderService orderService,
							ItemService itemService,
							UserService userService) {
		this.orderService = orderService;
		this.itemService = itemService;
		this.userService = userService;
	}

	@RequestMapping(value = {"/master"}, method = RequestMethod.GET)
	public String getMasterDashBoard(Model model) {
		List <Order> masterOrders = orderService.getAllAllowed(userService.getCurrentUser());
		model.addAttribute("masterOrders", masterOrders);
		return "masterView/MasterDashBoard";
	}

	@RequestMapping(value = {"/master/order/{id}"}, method = RequestMethod.GET)
	public String getOrderForm(@PathVariable("id") Long id, Model model) {
		Order order = orderService.get(id);
		model.addAttribute("order", order);
		return "masterView/MasterOrderForm";
	}

	@RequestMapping(value = {"/master/order/item/{id}"}, method = RequestMethod.GET)
	public String getItemForm(@PathVariable("id") Long id, Model model) {
		Item item = itemService.get(id);
		model.addAttribute(item);
		return "masterView/MasterItemForm";
	}

	@RequestMapping(value = {"/master/search"}, method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("masterView/MasterDashBoard");
		String searchItem = request.getParameter("searchItem");
		model.addObject("masterOrders", orderService.searchByAllFields(searchItem));
		return model;
	}

	@RequestMapping(value = {"/master/order/{id}/send"}, method = RequestMethod.GET)
	public ModelAndView changeStatus(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("masterView/MasterOrderForm");
		model.addObject("order", orderService.nextStatus(id));
		return model;
	}

	@RequestMapping(value = {"/master/order/{id}/money"}, method = RequestMethod.GET)
	public ModelAndView getPayment(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("masterView/MasterOrderForm");
		model.addObject("order", orderService.getPayment(id));
		return model;
	}

	@RequestMapping(value = {"/master/order/item/{id}/status"}, method = RequestMethod.GET)
	public ModelAndView changeItemStatus(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("masterView/MasterItemForm");
		Item newItem = itemService.changeStatus(id);
		itemService.save(newItem);
		model.addObject("item", newItem);
		return model;
	}
}