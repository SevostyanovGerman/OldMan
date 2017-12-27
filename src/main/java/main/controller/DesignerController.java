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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DesignerController {

	private static final Logger logger = LoggerFactory.getLogger(DesignerController.class);

	private OrderService orderService;
	private ItemService itemService;
	private ImageService imageService;
	private UserService userService;
	private NotificationService notificationService;

	@Autowired
	public DesignerController(OrderService orderService, ItemService itemService,
							  ImageService imageService, UserService userService,
							  NotificationService notificationService) {
		this.orderService = orderService;
		this.itemService = itemService;
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
	@RequestMapping(value = {"/designer/order/{orderId}"}, method = RequestMethod.GET)
	public ModelAndView order(@PathVariable("orderId") Long orderId) {
		ModelAndView model = new ModelAndView("/designerView/DesignerOrderForm");
		try {
			model.addObject("order", orderService.get(orderId));
		} catch (Exception e) {
			model = new ModelAndView("/designerView/DesignerDashBoard");
			logger.error("while retrieving order Id={}", orderId);
		}

		String user = userService.getCurrentUser().getName();
		List<Notification> myNotes = notificationService.findAllByUser(user);
		for (Notification n : myNotes) {
			if (n.getOrder() == orderService.get(orderId).getId()) {
				notificationService.delete(n.getId());
				model.addObject("tabIndex", "1");
			}
		}

		return model;
	}

	//Item page
	@RequestMapping(value = {"/designer/order/item/{orderId}/{itemId}"}, method = RequestMethod.GET)
	public ModelAndView item(@PathVariable("itemId") Long itemId,
							 @PathVariable("orderId") Long orderId) {
		ModelAndView model = new ModelAndView("/designerView/DesignerItemForm");
		try {
			model.addObject("item", itemService.get(itemId));
			model.addObject("order", orderService.get(orderId));
		} catch (Exception e) {
			logger.error("while retrieving item Id={}", itemId);
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}

	@RequestMapping(value = {"/designer/item/save/{orderId}/{itemId}"}, method = RequestMethod.POST)
	public ModelAndView save(@PathVariable("orderId") long orderId,
							 @PathVariable("itemId") long itemId,
							 MultipartHttpServletRequest uploadDesignerFiles) throws IOException, SQLException {
		List<Image> uploadImages = imageService.uploadAndSaveBlobFile(uploadDesignerFiles);
		Item item = itemService.get(itemId);
		if (uploadImages.size() > 0) {
			item.setStatus(true);
			item.getImages().addAll(uploadImages);
			itemService.save(item);
		}
		return new ModelAndView("redirect:/designer/order/item/" + orderId + "/" + itemId);
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

	//Удаление загруженного файла
	@RequestMapping(value = {"/designer/order/item/deleteFile/{orderId}/{itemId}/{imageId}"}, method = RequestMethod.GET)
	public ModelAndView deleteFile(@PathVariable("orderId") Long orderId,
								   @PathVariable("itemId") Long itemId,
								   @PathVariable("imageId") Long imageId) {
		Image file = imageService.get(imageId);
		imageService.delete(file);
		return new ModelAndView("redirect:/designer/order/item/" + orderId + "/" + itemId);
	}

	//Загрузка на компьютер всех файлов заказчика
	@RequestMapping(value = "/designer/downloadAllFiles/{orderId}/{itemId}", method = RequestMethod.GET)
	public ModelAndView downloadAllFiles(@PathVariable("orderId") Long orderId,
										 @PathVariable("itemId") Long itemId) {
		List<Image> customerFileList = itemService.get(itemId).getFiles();
		try {
			imageService.downloadAllFiles(customerFileList);
		} catch (IOException ioe) {
			logger.error("Ошибка в чтении файла: " + ioe);
		} catch (SQLException sqle) {
			logger.error("Ошибка выборки из базы данных: " + sqle);
		}
		return new ModelAndView("redirect:/designer/order/item/" + orderId + "/" + itemId);
	}

	//Загрузка на компьютер всех файлов дизайнера
	@RequestMapping(value = "/designer/downloadAllImages/{orderId}/{itemId}", method = RequestMethod.GET)
	public ModelAndView downloadAllImages(@PathVariable("orderId") Long orderId,
										  @PathVariable("itemId") Long itemId) {
		List<Image> designerFileList = itemService.get(itemId).getImages();
		try {
			imageService.downloadAllFiles(designerFileList);
		} catch (IOException | SQLException ex) {
			for (Image image : designerFileList) {
				logger.error("Error reading file from database: " + image.getFileName());
			}
		}
		return new ModelAndView("redirect:/designer/order/item/" + orderId + "/" + itemId);
	}

	//Загрузка на компьютер одного файла
	@RequestMapping(value = "/designer/downloadOneFile/{orderId}/{itemId}/{fileId}", method = RequestMethod.GET)
	public ModelAndView downloadOneFile(@PathVariable("orderId") Long orderId,
										@PathVariable("itemId") Long itemId,
										@PathVariable("fileId") Long fileId) {
		try {
			imageService.downloadOneFile(imageService.get(fileId));
		} catch (IOException | SQLException ex) {
			logger.error("Error reading file from database: " + imageService.get(fileId).getFileName());
		}
		return new ModelAndView("redirect:/designer/order/item/" + orderId + "/" + itemId);
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
		}
		model.addObject("orders", orders);
		return model;
	}
}
