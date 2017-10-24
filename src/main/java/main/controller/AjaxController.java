package main.controller;

import main.model.Customer;
import main.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
	public List <Customer> realCustomer(@RequestParam(value = "q") String q, Model model) {
		List <Customer> list = customerService.searchCustomer(q);
		return list;
	}
}
