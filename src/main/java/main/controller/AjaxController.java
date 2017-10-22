package main.controller;

import main.model.Customer;
import main.model.Order;
import main.service.CustomerService;
import main.service.OrderService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AjaxController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = {"/ajax"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List <String> ajax(@RequestParam(value = "q") String q, Model model) {
		List <Order> list = orderService.findByNumber(q);
		List <String> result = new ArrayList <>();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			result.add(list.get(i).getNumber());
		}
		return result;
	}

	@RequestMapping(value = {"/customersearch"}, method = RequestMethod.GET)
	public List <Customer> realCustomer(@RequestParam(value = "q") String q, Model model) {
		List <Customer> list = customerService.searchCustomer(q);
		return list;
	}
}
