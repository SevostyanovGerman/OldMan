package main.controller;

import main.model.File;
import main.model.*;
import main.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
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

	private FileService fileService;

	private final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@Autowired
	public ManagerController(OrderService orderService,
							 DeliveryService deliveryService,
							 CustomerService customerService,
							 ItemService itemService,
							 UserService userService,
							 StatusService statusService,
							 PaymentService paymentService,
							 FileService fileService) {
		this.orderService = orderService;
		this.deliveryService = deliveryService;
		this.customerService = customerService;
		this.itemService = itemService;
		this.userService = userService;
		this.statusService = statusService;
		this.paymentService = paymentService;
		this.fileService = fileService;
	}

	@RequestMapping(value = {"/manager"}, method = RequestMethod.GET)
	public ModelAndView getOrderList() {
		ModelAndView model = new ModelAndView("/managerView/ManagerDashBoard");
		User authUser = userService.getCurrentUser();
		List<Order> orderList = orderService.getAllAllowed(authUser);
		model.addObject("authUser", authUser);
		model.addObject("orderList", orderList);
		return model;
	}

	//Просмотр и редактирование существующего заказа
	@RequestMapping(value = {"/manager/order/update/{id}"}, method = RequestMethod.GET)
	public ModelAndView updateOrder(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		model.addObject("authUser", userService.getCurrentUser());
		model.addObject("order", orderService.get(id));
		model.addObject("statuses", statusService.getAll());
		model.addObject("designerList", userService.getByRole(2L)); // Как избавиться от 2?
		model.addObject("masterList", userService.getByRole(3L));   //Как избавиться от 3?
		model.addObject("newCustomer", new Customer());
		model.addObject("newDelivery", new Delivery());
		model.addObject("paymentList", paymentService.getAll());
		return model;
	}

	@RequestMapping(value = {"/manager/order/send/{id}"}, method = RequestMethod.GET)
	public ModelAndView sendOrder(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		model.addObject("order", orderService.nextStatus(id));
		return new ModelAndView("redirect:/manager/");
	}

	//Меняем статус заказа на новый
	@RequestMapping(value = {"/manager/order/send/{id}/to/{statusId}"}, method = RequestMethod.GET)
	public ModelAndView sendOrderTo(@PathVariable("id") Long id, @PathVariable("statusId") Long statusId) {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		model.addObject("order", orderService.changeStatusTo(id, statusId));
		return new ModelAndView("redirect:/manager/order/update/" + id);
	}

	//Меняем дизайнера заказа
	@RequestMapping(value = {"/manager/order/change/{id}/designer/{designerId}"}, method = RequestMethod.GET)
	public ModelAndView changeDesigner(@PathVariable("id") Long id, @PathVariable("designerId") Long designerId) {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		Order order = orderService.get(id);
		User designer = userService.get(designerId);
		order.setDesigner(designer);
		orderService.save(order);
		return new ModelAndView("redirect:/manager/order/update/" + id);
	}

	//Меняем мастера заказа
	@RequestMapping(value = {"/manager/order/change/{id}/master/{masterId}"}, method = RequestMethod.GET)
	public ModelAndView changeMaster(@PathVariable("id") Long id, @PathVariable("masterId") Long masterId) {
		ModelAndView model = new ModelAndView("/managerView/ManagerOrderForm");
		Order order = orderService.get(id);
		User master = userService.get(masterId);
		order.setMaster(master);
		orderService.save(order);
		return new ModelAndView("redirect:/manager/order/update/" + id);
	}

	//Создаём новый заказ с новой позицией
	@RequestMapping(value = {"/manager/order/add"}, method = RequestMethod.GET)
	public ModelAndView madeNewOrder() {
		ModelAndView model = new ModelAndView("/managerView/ManagerNewOrder");
		Item item = new Item();
		model.addObject("authUser", userService.getCurrentUser());
		model.addObject("item", item);
		return model;
	}

	//Сохраняем новый заказ с новой позицией
	@RequestMapping(value = {"/manager/item/saveNewOrder"}, method = RequestMethod.POST)
	public ModelAndView saveNewOrder(@ModelAttribute("item") Item item) throws IOException, SQLException {
		Order order = new Order(false, false, new Date(), statusService.getByNumber(1L), userService.getCurrentUser());
		orderService.save(order);
		order.setNumber(order.getId().toString());
		orderService.save(order);
		List<File> uploadCustomerFiles = fileService.uploadFile(item.getUploadCustomerFiles());
		item.setFiles(uploadCustomerFiles);
		item.setOrder(order);
		itemService.save(item);
		return new ModelAndView("redirect:/manager/order/update/" + order.getId());
	}

	//Удаляем заказ
	@RequestMapping(value = {"/manager/order/delete/{orderId}"}, method = RequestMethod.GET)
	public ModelAndView deleteOrder(@PathVariable("orderId") Long orderId) {
		Order order = orderService.get(orderId);
		orderService.deleteOrder(order);
		orderService.save(order);
		return new ModelAndView("redirect:/manager");
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
	public ModelAndView updateItem(@PathVariable("orderId") Long orderId, @PathVariable("itemId") Long itemId) {
		ModelAndView model = new ModelAndView("/managerView/ManagerItemForm");
		model.addObject("authUser", userService.getCurrentUser());
		model.addObject("order", orderService.get(orderId));
		model.addObject("item", itemService.get(itemId));
		return model;
	}

	//Сохраняем позицию (новую или обновлённую) в существующем заказе
	@RequestMapping(value = {"/manager/item/save/{orderId}"}, method = RequestMethod.GET)
	public ModelAndView save(@PathVariable("orderId") String orderId, @ModelAttribute("item") Item item) {
		Order order = orderService.get(Long.parseLong(orderId));
		item.setOrder(order);
		itemService.save(item);
		Long redirectOrderId = order.getId();
		return new ModelAndView("redirect:/manager/order/update/" + redirectOrderId);
	}

	//Удаляем позицию из заказа
	@RequestMapping(value = {"/manager/item/delete/{orderId}/{itemId}"}, method = RequestMethod.GET)
	public ModelAndView deleteItem(@PathVariable("orderId") Long orderId, @PathVariable("itemId") Long itemId) {
		itemService.delete(itemId);
		return new ModelAndView("redirect:/manager/order/update/" + orderId);
	}

	//Устанавливаем тип оплаты в существующем заказе
	@RequestMapping(value = {"/manager/order/setPaymentType/{orderId}/{paymentId}"}, method = RequestMethod.GET)
	public ModelAndView setPaymentType(@PathVariable("orderId") Long orderId,
									   @PathVariable("paymentId") Long paymentId) {
		Order order = orderService.get(orderId);
		Payment payment = paymentService.get(paymentId);
		order.setPaymentType(payment);
		orderService.save(order);
		return new ModelAndView("redirect:/manager/order/update/" + orderId);
	}

	//Создание нового клиента
	@RequestMapping(value = {"/manager/order/addcustomer/{orderId}"}, method = RequestMethod.POST)
	public ModelAndView addCustomer(@PathVariable("orderId") Long orderId,
									@ModelAttribute("newCustomer") Customer customer,
									@ModelAttribute("newDelivery") Delivery delivery) {
		Order order = orderService.get(orderId);
		try {
			deliveryService.save(delivery);
			customerService.save(customer);
			customer.getDeliveries().add(delivery);
			order.setCustomer(customer);
			order.setDelivery(customer.getDefaultDelivery());
			orderService.save(order);
		} catch (DataIntegrityViolationException e) {
			logger.info("Пользователь существует");
		} catch (Exception e) {
			return new ModelAndView("redirect:/manager/order/update/" + orderId);
		}
		return new ModelAndView("redirect:/manager/order/update/" + orderId);
	}

	//Выбор/изменения клиента в заказе
	@RequestMapping(value = {"/manager/order/changeCustomer/{orderId}"}, method = RequestMethod.POST)
	public ModelAndView changeCustomer(@PathVariable("orderId") Long orderId,
									   @ModelAttribute("firstNameField") String fName,
									   @ModelAttribute("secNameField") String sName,
									   @ModelAttribute("emailField") String eMail,
									   @ModelAttribute("phoneField") String phone) {
		Order order = orderService.get(orderId);
		try {
			Customer customer = customerService.getByEmail(eMail);
			if (customer == null) {
				customer = order.getCustomer();
			}
			customer.updateCustomerFields(fName, sName, eMail, phone);
			customerService.save(customer);
			order.setDelivery(customer.getDefaultDelivery());
			order.setCustomer(customer);
			orderService.save(order);
		} catch (Exception e) {
			logger.error("Ошибка изменения покупателя");
		}
		return new ModelAndView("redirect:/manager/order/update/" + orderId);
	}

	//Изменение создание адреса доставки
	@RequestMapping(value = {"/manager/order/editDelivery/{orderId}"}, method = RequestMethod.POST)
	public ModelAndView changeAddress(@PathVariable("orderId") Long orderId,
									  @ModelAttribute("editAddressCountry") String country,
									  @ModelAttribute("editAddressCity") String city,
									  @ModelAttribute("editAddressAddress") String address,
									  @ModelAttribute("editAddressZip") String zip) {
		Order order = orderService.get(orderId);
		try {
			Delivery delivery = new Delivery(country, city, address, zip);
			deliveryService.save(delivery);
			order.setDelivery(delivery);
			orderService.save(order);
		} catch (Exception e) {
			logger.warn("Не удалось изменить адрес доставки");
		}
		return new ModelAndView("redirect:/manager/order/update/" + orderId);
	}

	//Загрузка файлов заказчика в существующую позицию
	@RequestMapping(value = "/uploadCustomerFile/", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public void uploadSampleFiles(@RequestParam(value = "id") Long itemId, HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<File> uploadedCustomerFiles = fileService.saveBlobFile(multipartRequest);
		Item item = itemService.get(itemId);
		List<File> customerFiles = item.getFiles();
		customerFiles.addAll(uploadedCustomerFiles);
		item.setFiles(customerFiles);
		itemService.save(item);
	}

	//Удаление загруженного файла заказчик
	@RequestMapping(value = {"/manager/order/item/deleteFile/{orderId}/{itemId}/{fileId}"}, method = RequestMethod.GET)
	public ModelAndView delImage(@PathVariable("orderId") Long orderId,
								 @PathVariable("itemId") Long itemId,
								 @PathVariable("fileId") Long fileId) throws IOException {
		File file = fileService.get(fileId);
		fileService.delete(file);
		return new ModelAndView("redirect:/manager/item/update/"+ orderId + "/" + itemId);
	}

	@RequestMapping(value = {"/manager/customers"}, method = RequestMethod.GET)
	public ModelAndView getCustomerList() {
		ModelAndView model = new ModelAndView("/managerView/ManagerCustomerDashBoard");
		User authUser = userService.getCurrentUser();
		List<Customer> customerList = customerService.getAll();
		model.addObject("authUser", authUser);
		model.addObject("customerList", customerList);
		return model;
	}
}
