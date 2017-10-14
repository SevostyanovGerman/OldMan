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
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = {"/ajax"}, method = RequestMethod.GET)
	public List<Order> ajax (@RequestParam(value="q") String q, Model model) {

		List<Order> list = orderService.findByNumber(q);
		List<String> result = new ArrayList <>();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < list.size() ; i++) {
			//stringBuilder.append("<a href=\"test \">" + list.get(i).getNumber() + "</a><br> \n");
			result.add(list.get(i).getNumber());
		}

		return list;
	}
}
