package main.controller;

import main.model.Comment;
import main.model.Item;
import main.model.Order;
import main.service.CommentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class MasterController {

	private final Logger logger = LoggerFactory.getLogger(DesignerController.class);

	private OrderService orderService;

	private ItemService itemService;

	private UserService userService;

	private CommentService commentService;

	@Autowired
	public MasterController(OrderService orderService, ItemService itemService, UserService userService,
							CommentService commentService) {
		this.orderService = orderService;
		this.itemService = itemService;
		this.userService = userService;
		this.commentService = commentService;
	}

	@RequestMapping(value = {"/master"}, method = RequestMethod.GET)
	public ModelAndView getMasterDashBoard() {
		ModelAndView model = new ModelAndView("masterView/MasterDashBoard");
		try {
			List<Order> masterOrders = orderService.getAllAllowed(userService.getCurrentUser());
			model.addObject("masterOrders", masterOrders);
		} catch (Exception e) {
			logger.error("Controller '/master' orderService.masterOrders() error");
		}
		return model;
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

	@RequestMapping(value = {"/master/order/{orderId}/item/{itemId}"}, method = RequestMethod.GET)
	public String getItemForm(@PathVariable("itemId") Long itemId, @PathVariable("orderId") Long orderId, Model
		model) {
		try {
			Item item = itemService.get(itemId);
			model.addAttribute(item);
			model.addAttribute("order", orderService.get(orderId));
		} catch (Exception e) {
			logger.error("Controller '/master/order/item/', itemId={}", itemId);
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
	public ModelAndView getPayment(@PathVariable Long id, HttpServletRequest request) {
		String url;
		try {
			String referer = request.getHeader("referer");
			url = getUrl(referer);
		} catch (Exception e) {
			url = "/403";
			return new ModelAndView("redirect:" + url);
		}
		try {
			orderService.getPayment(id);
		} catch (Exception e) {
			logger.warn("You couldn't get the money for order");
		}
		return new ModelAndView("redirect:" + url);
	}

	@RequestMapping(value = {"/master/order/{orderId}/item/{itemId}/status"}, method = RequestMethod.GET)
	public ModelAndView changeItemStatus(@PathVariable("itemId") Long itemId, @PathVariable("orderId") Long orderId) {
		try {
			Item newItem = itemService.changeStatus(itemId);
			itemService.save(newItem);
			//model.addObject("item", newItem);
		} catch (Exception e) {
			logger.warn("You couldn't change the status of item");
			new ModelAndView("masterView/MasterItemForm");
		}
		return new ModelAndView("redirect:/master/order/" + orderId + "/item/" + itemId);
	}

	@RequestMapping(value = {"/master/order/comment/{id}"}, method = RequestMethod.POST)
	public ModelAndView addComment(@PathVariable Long id, @RequestParam(value = "comment") String content) {
		ModelAndView model = new ModelAndView("masterView/MasterOrderForm");
		Comment comment = new Comment(content, userService.getCurrentUser().toString(), new Date());
		commentService.save(comment);
		Order order = orderService.get(id);
		order.getComments().add(comment);
		orderService.save(order);
		model.addObject("order", order);
		return model;
	}

	private String getUrl(String referer) {
		String url;
		int index = referer.indexOf("?");
		if (index > 0) {
			url = referer.substring(0, index);
		} else {
			url = referer.toString();
		}
		return url;
	}
}