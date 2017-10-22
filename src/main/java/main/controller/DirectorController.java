package main.controller;

import main.service.OrderService;
import main.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DirectorController {
	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	private final Logger logger = LoggerFactory.getLogger(MainController.class);

	@RequestMapping(value = {"/director"}, method = RequestMethod.GET)
	public ModelAndView director() {
		ModelAndView model = new ModelAndView("/directorView/DirectorDashBoard");
		try {
			model.addObject("orders", orderService.getAll());
		} catch (Exception e) {
			logger.error("Can\'t get all orders", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/stuff"}, method = RequestMethod.GET)
	public ModelAndView showStaff() {
		ModelAndView model = new ModelAndView("/directorView/DirectorStuffBoard");
		try {
			model.addObject("stuff", userService.getAllUsers());
		} catch (Exception e) {
			logger.error("Can\'t get stuff list", e);
		}
		return model;
	}
}
