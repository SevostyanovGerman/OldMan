package main.controller;

import main.model.Order;
import main.model.User;
import main.service.OrderService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AjaxController {
	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;

	@RequestMapping(value = {"/ajax"}, method = RequestMethod.GET)

	public String ajax (@RequestParam(value="q") String q, Model model) {

		List<Order> list = orderService.findByNumber(q);

		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < list.size() ; i++) {
			stringBuilder.append("<a href=\"test \">" + list.get(i).getNumber() + "</a><br> \n");
		}

		return stringBuilder.toString();
	}
}
