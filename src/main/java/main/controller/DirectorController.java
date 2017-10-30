package main.controller;

import main.model.Status;
import main.service.OrderService;
import main.service.StatusService;
import main.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DirectorController {

	private UserService userService;

	private OrderService orderService;

	private StatusService statusService;

	private final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	public DirectorController (UserService userService, OrderService orderService,StatusService statusService){
		this.userService = userService;
		this.orderService = orderService;
		this.statusService = statusService;
	}

	@RequestMapping(value = {"/director"}, method = RequestMethod.GET)
	public ModelAndView director () {
		ModelAndView model = new ModelAndView("/directorView/DirectorDashBoard");
		try {
			model.addObject("orders", orderService.getAll());
		} catch (Exception e) {
			logger.error("Can\'t get all orders", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/stuff"}, method = RequestMethod.GET)
	public ModelAndView showStaff () {
		ModelAndView model = new ModelAndView("/directorView/DirectorStuffBoard");
		try {
			model.addObject("stuff", userService.getAllUsers());
		} catch (Exception e) {
			logger.error("Can\'t get stuff list", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/controlpanel"}, method = RequestMethod.GET)
	public ModelAndView controlPanel () {
		ModelAndView model = new ModelAndView("/directorView/ControlPanel");

		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/statuses"}, method = RequestMethod.GET)
	public ModelAndView controlPanelStatus () {
		ModelAndView model = new ModelAndView("/directorView/ControlPanelStatus");
		try {
			model.addObject("statuses", statusService.getAll());
			model.addObject("newstatus", new Status());
		} catch (Exception e) {
			logger.error("Can\'t get status list", e);
		}

		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/statuses/create"}, method = RequestMethod.POST)
	public String createStatus (@ModelAttribute("status") Status status) {
		try {
			statusService.save(status);
		} catch (Exception e) {
			logger.error("Can\'t save status", e);
		}

		return "redirect:/director/controlpanel/statuses";
	}

	@RequestMapping(value = {"/director/controlpanel/status/delete/{id}"}, method = RequestMethod.GET)
	public String deleteStatus (@PathVariable("id") Long id) {
		logger.info("Deleting status with id: ", id);
		Status deletedStatus = null;
		try {
			deletedStatus = statusService.get(id);
		} catch (Exception e){
			logger.error("Can\'t get status with id: ", id);
		}

		if (deletedStatus != null){
			deletedStatus.setDeleted(1);
			try {
				statusService.save(deletedStatus);
			} catch (Exception e){
				logger.error("Can\'t remove from DB status with id: ", id);
			}
		}

		return "redirect:/director/controlpanel/statuses";
	}

}
