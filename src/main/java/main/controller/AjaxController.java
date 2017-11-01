package main.controller;

import main.model.Customer;
import main.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class AjaxController {

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = {"/customersearch"}, method = RequestMethod.GET)
	public List <Customer> realCustomer(@RequestParam(value = "q") String q) {
		List <Customer> list = customerService.searchCustomer(q);
		return list;
	}

	@RequestMapping(value = {"/checkuser"}, method = RequestMethod.GET)
	public String checkUser(@RequestParam(value = "q") String q) {
		return customerService.checkEmail(q).toString();
	}

	@RequestMapping(value = {"/getCustomer"}, method = RequestMethod.GET)
	public Customer getCustomer(@RequestParam(value = "q") Long q) {
		return customerService.get(q);
	}
}
