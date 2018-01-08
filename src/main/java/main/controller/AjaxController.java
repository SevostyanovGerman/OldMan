package main.controller;

import main.model.Customer;
import main.model.Mail;
import main.model.Mail.MailNames;
import main.model.Notification;
import main.model.Order;
import main.model.User;
import main.service.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class AjaxController {

	private CustomerService customerService;
	private OrderService orderService;
	private UserService userService;
	private NotificationService notificationService;
	private MailService mailService;

	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public AjaxController(CustomerService customerService, OrderService orderService, UserService userService,
						  NotificationService notificationService, MailService mailService) {
		this.customerService = customerService;
		this.orderService = orderService;
		this.userService = userService;
		this.notificationService = notificationService;
		this.mailService = mailService;
	}

	//поиск клиентов в форме managerOrder
	@RequestMapping(value = {"/customersearch"}, method = RequestMethod.GET)
	public List<Customer> realCustomer(@RequestParam(value = "q") String name) {
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

	//Выбор клиента//
	@RequestMapping(value = "/selectCustomer/{customerId}/{orderId}", method = RequestMethod.POST)
	public void selectCustomer(@PathVariable("customerId") Long customerId, @PathVariable("orderId") Long orderId) {
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
	public List<Object> statisticGeoOrder(Date startDate, Date endDate) {
		try {
			DateTime start = new DateTime(startDate);
			DateTime end = new DateTime(endDate).withHourOfDay(23).withMinuteOfHour(59);
			return orderService.statisticGeo(start.toDate(), end.toDate());
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
			return userService.getUsersByNameLike(name).stream().map(User::getName).filter(newName -> !newName.equals(userService.getCurrentUser().getName())).collect(Collectors.toList());
	}

	//Контроллер возвращающий список уведомлений для конкретного пользователя
	@RequestMapping(value = "/notifications/get", method = RequestMethod.GET)
	public List<Notification> getNotifications() {
		try {
			return notificationService.findAllByUser(userService.getCurrentUser().getName());
		} catch (Exception e) {
			logger.error("while getting notification for current user");
			return null;
		}
	}

	//Добавить аватар на профиль
	@RequestMapping(value = {"/profile/avatar"}, method = RequestMethod.POST)
	public void addAvatar(@RequestParam("0") MultipartFile file) {
		User user = userService.getCurrentUser();
		try {
			userService.addAvatar(file, user);
		} catch (Exception e) {
			logger.error("while saving avatar image for profile");
		}
	}

	//Изменение пароля
	@RequestMapping(value = {"/profile/password"}, method = RequestMethod.POST)
	public String changePassword(String currentPassword, String newPassword) {
		User user = userService.getCurrentUser();

		if (newPassword.length() < 3) {
			return "Слишком короткий пароль, минимум 3 символа";
		}
		try {
			if (passwordEncoder.matches(currentPassword, user.getPassword())) {
				userService.setPasswordEncoder(user, newPassword);
				userService.save(user);
				return "Пароль изменен";
			} else {
				return "Не верный текущий пароль";
			}

		} catch (Exception e) {
			logger.error("while changing password");
			return "ошибка при смене пароля";
		}
	}

	//Восстановление пароля по почте
	@RequestMapping(value = {"/forgotten/mail/"}, method = RequestMethod.POST)
	public String forgotPasswordByMail(String email) throws MessagingException {
		User user;
		try {
			if (email.length() > 0) {
				user = userService.getByEmail(email);
			} else {
				return "Введите ваш email";
			}
			if (user != null) {
				SecureRandom random = new SecureRandom();
				byte bytes[] = new byte[100];
				random.nextBytes(bytes);
				String token = bytes.toString() + UUID.randomUUID().toString();
				user.setToken(token);
				DateTime expire = new DateTime().plusHours(1);
				user.setTokenExpire(expire.toDate());
				userService.save(user);
				Mail mail = mailService.getByMailName(MailNames.RESET_PASSWORD);
				mail.setForUser(user);
				mail.setTitleParametr(user.toString());
				mailService.sendEmail(mail);
				return "Новый пароль отправлен на вашу почту";
			}
		} catch (Exception e) {
			logger.error("While reset password by mail");
		}

		return "Пользователь не найден";

	}

	//Изменение пароля по токену
	@RequestMapping(value = {"/profile/newPassword"}, method = RequestMethod.POST)
	public String newPasswordByToken(String newPassword, String token) {
		User user = userService.getByToken(token);
		if (user == null) {
			return "токен не действителен";
		}
		if (newPassword.length() < 3) {
			return "Слишком короткий пароль, минимум 3 символа";
		}
		try {
			userService.setPasswordEncoder(user, newPassword);
			user.setTokenExpire(null);
			user.setToken(null);
			userService.save(user);
			return "Пароль изменен";

		} catch (Exception e) {
			logger.error("while changing password");
			return "ошибка при смене пароля";
		}
	}
}



