package main.controller;

import main.model.Image;
import main.model.Item;
import main.model.Notification;
import main.model.Order;
import main.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DesignerController {

	private static final Logger logger = LoggerFactory.getLogger(DesignerController.class);

	private OrderService orderService;

	private ItemService itemService;

	private CommentService commentService;

	private ImageService imageService;

	private UserService userService;

	private NotificationService notificationService;

	@Autowired
	public DesignerController(OrderService orderService, ItemService itemService,
							  CommentService commentService, ImageService imageService,
							  UserService userService, NotificationService notificationService) {
		this.orderService = orderService;
		this.itemService = itemService;
		this.commentService = commentService;
		this.imageService = imageService;
		this.userService = userService;
		this.notificationService = notificationService;
	}

	//Designer dashboard
	@RequestMapping(value = {"/designer"}, method = RequestMethod.GET)
	public ModelAndView designer() {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		try {
			model.addObject("orders", orderService.getAllAllowed(userService.getCurrentUser()));
		} catch (Exception e) {
			logger.error("while retrieving order List");
		}
		return model;
	}

	//Order page
	@RequestMapping(value = {"/designer/order/{id}"}, method = RequestMethod.GET)
	public ModelAndView order(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		try {
			model.addObject("order", orderService.get(id));
		} catch (Exception e) {
			model = new ModelAndView("/designerView/DesignerDashBoard");
			logger.error("while retrieving order Id={}", id);
		}

		String user = userService.getCurrentUser().getName();
		List<Notification> myNotes = notificationService.findAllByUser(user);
		for (Notification n : myNotes) {
			notificationService.delete(n.getId());
		}

		return model;
	}

	//Item page
	@RequestMapping(value = {"/designer/order/{orderId}/item/{itemId}"}, method = RequestMethod.GET)
	public ModelAndView item(@PathVariable("itemId") Long itemId,
							 @PathVariable("orderId") Long orderId) {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		try {
			model.addObject("item", itemService.get(itemId));
			model.addObject("order", orderService.get(orderId));
		} catch (Exception e) {
			logger.error("while retrieving item Id={}", itemId);
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}

	//Изменение статуса товара
	@RequestMapping(value = {"/designer/order/item/save/{id}"}, method = RequestMethod.POST)
	public ModelAndView save(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		try {
			Item item = itemService.get(id);
			itemService.changeStatus(item.getId());
			itemService.save(item);
			model.addObject("item", item);
		} catch (Exception e) {
			logger.error("while changing item status id={}", id);
			return new ModelAndView("redirect:/designer/order/" + id);
		}
		return model;
	}

	//Смена статуса заказа
	@RequestMapping(value = {"/designer/send/order={id}"}, method = RequestMethod.POST)
	public ModelAndView send(@PathVariable Long id) {
		try {
			orderService.nextStatus(id);
		} catch (Exception e) {
			logger.warn("while changing order status id={}", id);
			return new ModelAndView("redirect:/designer/");
		}
		return new ModelAndView("redirect:/designer/");
	}

	//Удаление картинки дизайнера
	@RequestMapping(value = {"/designer/order/item/delimage/{orderId}/{itemId}/{imageId}"},
					method = RequestMethod.POST)
	public ModelAndView delImage(@PathVariable("imageId") Long imageId,
								 @PathVariable("itemId") Long itemId,
								 @PathVariable("orderId") Long orderId) throws IOException {
		try {
			Image image = imageService.get(imageId);
			imageService.delete(image);
			return new ModelAndView("redirect:/designer/order/" + orderId + "/item/" + itemId);
		} catch (Exception e) {
			logger.error("while deleting image status id={}", imageId);
			return new ModelAndView("/designerView/DesignerDashBoard");
		}
	}

	//Выборка тех заказов где есть уведомления для конкретного пользователя
	@RequestMapping(value = {"/order/designer/notification/get"}, method = RequestMethod.GET)
	public ModelAndView getOrdersByNotification() {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		String user = userService.getCurrentUser().getName();
		List<Notification> myNotes = notificationService.findAllByUser(user);
		List<Order> orders = new ArrayList<>();
		for (Notification n : myNotes) {
			orders.add(orderService.get(n.getOrder()));
			model.addObject("orders", orders);
		}
		return model;
	}
}
