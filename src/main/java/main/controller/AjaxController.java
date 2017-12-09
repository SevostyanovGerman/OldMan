package main.controller;

import main.model.Customer;
import main.model.Notification;
import main.model.Order;
import main.model.User;
import main.service.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AjaxController {

	private CustomerService customerService;
	private OrderService orderService;
	private ImageService imageService;
	private UserService userService;
	private NotificationService notificationService;

	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);

	private static final DateTimeFormatter DATE_TIME_FORMATTER =
		DateTimeFormat.forPattern("yyyy/mm/dd");

	@Autowired
	public AjaxController(CustomerService customerService, OrderService orderService,
						  ImageService imageService, UserService userService,
						  NotificationService notificationService) {
		this.customerService = customerService;
		this.orderService = orderService;
		this.imageService = imageService;
		this.userService = userService;
		this.notificationService = notificationService;
	}

	//поиск клиентов в форме managerOrder
	@RequestMapping(value = {"/customersearch"}, method = RequestMethod.GET)
	public List<Customer> realCustomer(@RequestParam(value = "q") String name, Model model) {
		try {
			return customerService.searchCustomer(name);
		} catch (Exception e) {
			logger.error("while retrieving customer list by name={}", name);
			return null;
		}
	}

	@RequestMapping(value = {"master/ordersByRange"}, method = RequestMethod.POST)
	public ResponseEntity<List<Order>> getOrderByRange(Date startDate, Date endDate) {
		List<Order> orderRange = orderService.findOrdersByRange(startDate, endDate);
		if (orderRange.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(orderRange, HttpStatus.OK);
	}

	//проверка email при создании клиента
	@RequestMapping(value = {"/checkuser"}, method = RequestMethod.GET)
	public String checkUser(@RequestParam(value = "email") String email) {
		try {
			return customerService.checkEmail(email).toString();
		} catch (Exception e) {
			logger.error("while checking already existing email={}", email);
			return "false";
		}
	}

	//Загрузка файлов//
	@RequestMapping(value = "/uploadImage/{id}", method = RequestMethod.POST)
	public void uploadSampleFiles(@PathVariable("id") Long itemId,
								  MultipartHttpServletRequest multipartHttpServletRequest) {
		try {
			List<MultipartFile> files = multipartHttpServletRequest.getFiles("files");
			imageService.saveBlobImagesToItem(files, itemId);
		} catch (Exception e) {
			logger.error("while saving file to item, id={}", itemId);
		}
	}

	//Выбор клиента//
	@RequestMapping(value = "/selectCustomer/{customerId}/{orderId}", method = RequestMethod.POST)
	public void selectCustomer(@PathVariable("customerId") Long customerId,
							   @PathVariable("orderId") Long orderId) {
		try {
			Order order = orderService.get(orderId);
			Customer customer = customerService.get(customerId);
			order.setCustomer(customer);
			if (order.getDeliveryType() == null || !order.getDeliveryType().getPickup()) {
				order.setDelivery(customer.getDefaultDelivery());
			}
			orderService.save(order);
		} catch (Exception e) {
			logger.error("while selecting customer, customer id={}", customerId);
		}
	}

	//Данные для статистики средней цены заказа
	//Средняя сумма заказа//
	@RequestMapping(value = "/statistic/middle/averageOrderPrice", method = RequestMethod.GET)
	public List<Object> statisticAverageOrderPrice(Date startDate, Date endDate, String dwm) {
		try {
			DateTime start = new DateTime(startDate);
			DateTime end = new DateTime(endDate).withHourOfDay(23).withMinuteOfHour(59);
			return orderService.avgPriceByMonth(start.toDate(), end.toDate(), dwm);
		} catch (Exception e) {
			logger.error("while retrieving list of orders for 'average orders statistic");
			return null;
		}
	}

	// сумма заказа статистика//
	@RequestMapping(value = "/statistic/sumOrderPrice", method = RequestMethod.GET)
	public List<Object> statisticSumOrderPrice(Date startDate, Date endDate, String dwm) {
		try {
			DateTime start = new DateTime(startDate);
			DateTime end = new DateTime(endDate).withHourOfDay(23).withMinuteOfHour(59);
			return orderService.sumPriceOrders(start.toDate(), end.toDate(), dwm);
		} catch (Exception e) {
			logger.error("while retrieving list of orders for 'sum orders statistic");
			return null;
		}
	}

	//Статистика гео//
	@RequestMapping(value = "/statistic/geo/getGeoObjects", method = RequestMethod.GET)
	public List<Object> statisticGeoOrder() {
		try {
			return orderService.statisticGeo();
		} catch (Exception e) {
			logger.error("while retrieving list of orders for 'geo statistic");
			return null;
		}
	}

	//Новые клиенты//
	@RequestMapping(value = "/statistic/newCustomers/ajaxData", method = RequestMethod.GET)
	public List<Object> statisticNewCustomers(Date startDate, Date endDate) {
		try {
			DateTime start = new DateTime(startDate);
			DateTime end = new DateTime(endDate).withHourOfDay(23).withMinuteOfHour(59);
			return orderService.statisticNewCustomers(start.toDate(), end.toDate());
		} catch (Exception e) {
			logger.error("while retrieving list of orders for 'new customers statistic");
			return null;
		}
	}

	//Контроллер возвращающий пользователей по typehead
	@RequestMapping(value = "/users/get/{name}", method = RequestMethod.GET)
	public List<String> getUsersByNameLike(@PathVariable String name) {
		return userService.getUsersByNameLike(name).stream().map(User::getName)
			.collect(Collectors.toList());
	}

	//Контроллер возвращающий список уведомлений для конкретного пользователя
	@RequestMapping(value = "/notifications/get", method = RequestMethod.GET)
	public List<Notification> getNotifications() {
		return notificationService.findAllByUser(userService.getCurrentUser().getName());
	}
}



