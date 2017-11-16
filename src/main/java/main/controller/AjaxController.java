package main.controller;

import main.model.Customer;
import main.model.Order;
import main.service.CustomerService;
import main.service.ImageService;
import main.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
public class AjaxController {

	private CustomerService customerService;

	private OrderService orderService;

	private ImageService imageService;

	@Autowired
	public AjaxController(CustomerService customerService, OrderService orderService, ImageService imageService) {
		this.customerService = customerService;
		this.orderService = orderService;
		this.imageService = imageService;
	}

	//поиск клиентов в форме managerOrder
	@RequestMapping(value = {"/customersearch"}, method = RequestMethod.GET)
	public List<Customer> realCustomer(@RequestParam(value = "q") String q, Model model) {
		List<Customer> list = customerService.searchCustomer(q);
		return list;
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
		return customerService.checkEmail(email).toString();
	}

	//Загрузка файлов//
	@RequestMapping(value = "/uploadImage/{id}", method = RequestMethod.POST)
	public void uploadSampleFiles(@PathVariable("id") Long itemId,
								  MultipartHttpServletRequest multipartHttpServletRequest) {
		List<MultipartFile> files = multipartHttpServletRequest.getFiles("files");
		if (imageService.saveBlobImage(files, itemId)) {
		}
	}
}



