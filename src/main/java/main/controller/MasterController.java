package main.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import main.Helpers;
import main.model.Image;
import main.model.Item;
import main.model.Notification;
import main.model.Order;
import main.model.User;
import main.service.ImageService;
import main.service.ItemService;
import main.service.NotificationService;
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

@Controller
public class MasterController {

	private final static Logger logger = LoggerFactory.getLogger(DesignerController.class);

	private OrderService orderService;

	private ItemService itemService;

	private UserService userService;

	private NotificationService notificationService;

	private ImageService imageService;

	@Autowired
	public MasterController(OrderService orderService, ItemService itemService, UserService userService,
		NotificationService notificationService, ImageService imageService) {
		this.orderService = orderService;
		this.itemService = itemService;
		this.userService = userService;
		this.notificationService = notificationService;
		this.imageService = imageService;
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
			User currentUser = userService.getCurrentUser();
			List<Order> orderList = orderService.getAllAllowed(currentUser);
			//проверка доступа к заказу. с учетом уведомлений
			if (!orderList.contains(order)) {
				List<Notification> orderNotification = notificationService.getByUserAndOrder(id, currentUser.getName());
				if (orderNotification.size() <= 0) {
					return "redirect: /master";
				}
			}

			String user = userService.getCurrentUser().getName();
			List<Notification> myNotes = notificationService.findAllByUser(user);
			for (Notification n : myNotes) {
				if (n.getOrder() == order.getId()) {
					notificationService.delete(n.getId());
					model.addAttribute("tabIndex", "1");
				}
			}
			model.addAttribute("order", order);

		} catch (Exception e) {
			logger.error("Controller '/master/order/', orderId={}", id);
		}
		return "masterView/MasterOrderForm";
	}

	@RequestMapping(value = {"/master/order/{orderId}/item/{itemId}"}, method = RequestMethod.GET)
	public String getItemForm(@PathVariable("itemId") Long itemId, @PathVariable("orderId") Long orderId, Model model) {
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
			//model.addObject("masterOrders", orderService.searchByAllFields(searchItem));
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
			url = Helpers.getUrl(request.getHeader("referer"));
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
		return new ModelAndView("redirect:/master/order/" + orderId);
	}

	//Выборка тех заказов где есть уведомления для конкретного пользователя
	@RequestMapping(value = {"/master/order/notification/get"}, method = RequestMethod.GET)
	public ModelAndView getOrdersByNotification() {
		ModelAndView model = new ModelAndView("/masterView/MasterDashBoard");
		String user = userService.getCurrentUser().getName();
		List<Notification> myNotes = notificationService.findAllByUser(user);
		List<Order> masterOrders = new ArrayList<>();
		for (Notification n : myNotes) {
			masterOrders.add(orderService.get(n.getOrder()));
		}
		model.addObject("masterOrders", masterOrders);
		return model;

	}

	//Загрузка на компьютер всех файлов дизайнера
	@RequestMapping(value = "/master/downloadAllImages/{orderId}/{itemId}", method = RequestMethod.GET)
	public ModelAndView downloadAllImages(@PathVariable("orderId") Long orderId, @PathVariable("itemId") Long itemId) {
		List<Image> designerFileList = itemService.get(itemId).getImages();
		try {
			imageService.downloadAllFiles(designerFileList);
		} catch (IOException | SQLException ex) {
			for (Image image : designerFileList) {
				logger.error("Error reading file from database: " + image.getFileName());
			}
		}
		return new ModelAndView("redirect:/master/order/" + orderId + "/item/" + itemId);
	}

	//Загрузка на компьютер одного файла
	@RequestMapping(value = "/master/downloadOneFile/{orderId}/{itemId}/{fileId}", method = RequestMethod.GET)
	public ModelAndView downloadOneFile(@PathVariable("orderId") Long orderId, @PathVariable("itemId") Long itemId,
		@PathVariable("fileId") Long fileId) {
		try {
			imageService.downloadOneFile(imageService.get(fileId));
		} catch (IOException | SQLException ex) {
			logger.error("Error reading file from database: " + imageService.get(fileId).getFileName());
		}
		return new ModelAndView("redirect:/master/order/" + orderId + "/item/" + itemId);
	}
}