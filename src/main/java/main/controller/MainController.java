package main.controller;

import main.Helpers;
import main.model.*;
import main.service.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class MainController {

	private final static Logger logger = LoggerFactory.getLogger(MainController.class);

	private CommentService commentService;

	private OrderService orderService;

	private UserService userService;

	private NotificationService notificationService;

	private MailService mailService;

	@Autowired
	public MainController(CommentService commentService, OrderService orderService, UserService userService,
						  NotificationService notificationService, MailService mailService) {
		this.commentService = commentService;
		this.orderService = orderService;
		this.userService = userService;
		this.notificationService = notificationService;
		this.mailService = mailService;
	}

	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
							  @RequestParam(value = "logout", required = false) String logout) {
		try {
			ModelAndView model = new ModelAndView();
			if (error != null) {
				model.addObject("error", "Invalid username and password!");
				logger.info("Invalid username and password");
			}
			if (logout != null) {
				model.addObject("msg", "You've been logged out successfully.");
			}
			model.setViewName("login");
			return model;
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = {"/403"}, method = RequestMethod.GET)
	public ModelAndView page403() {
		return new ModelAndView("403");
	}

	//Добавление комментария
	@RequestMapping(value = {"/order/comment/add={id}"}, method = RequestMethod.POST)
	public ModelAndView addComment(@PathVariable Long id, @ModelAttribute("commentText") String content,
								   @ModelAttribute("recipient") String recipient,
								   @ModelAttribute("commentBtnOrder") String commentId, HttpServletRequest request) {
		String url = Helpers.getUrl(request.getHeader("referer"));
		ModelAndView model = new ModelAndView("redirect:" + url);
		try {
			Comment comment;
			if (!commentId.isEmpty()) {
				Comment parent = commentService.get(Long.parseLong(commentId));
				comment = new Comment(content, userService.getCurrentUser(), recipient, new Date(), parent);
			} else {
				comment = new Comment(content, userService.getCurrentUser(), recipient, new Date());
			}
			commentService.save(comment);
			Order order = orderService.get(id);
			order.addComment(comment);
			orderService.save(order);
			Notification notification =
				new Notification(recipient.equals("") ? comment.getParent().getCreatedBy().getName() : recipient,
					order.getId());
			notificationService.save(notification);
			model.addObject("order", order);
			model.addObject("tabIndex", 1);
		} catch (Exception e) {
			logger.error("Ошибка при создании комментария, заказ id={}", e);
		}
		return model;
	}

	@RequestMapping(value = {"/order/comment/delete={id}"}, method = RequestMethod.GET)
	public ModelAndView deleteComment(@PathVariable Long id, HttpServletRequest request) {
		String url = Helpers.getUrl(request.getHeader("referer"));
		ModelAndView model = new ModelAndView("redirect:" + url);
		Comment comment = commentService.get(id);
		commentService.delete(comment);
		notificationService.delete(comment.getId());
		return model;
	}

	@RequestMapping(value = {"/order/comment/edit={id}"}, method = RequestMethod.POST)
	public ModelAndView editComment(HttpServletRequest request, @PathVariable Long id,
									@RequestParam("editText") String content) {
		String url = Helpers.getUrl(request.getHeader("referer"));
		ModelAndView model = new ModelAndView("redirect:" + url);
		Comment comment = commentService.get(id);
		if (!comment.getSentTo().equals(userService.getCurrentUser().getName())) {
			comment.setContent(content);
			commentService.edit(comment);
		} else {
			return page403();
		}
		return model;
	}

	@RequestMapping(value = "/orders/search", method = RequestMethod.POST)
	public String orderSearch(String search, Date startDate, Date endDate, Model model, String sort, Double minPrice,
							  Double maxPrice, int pageNumber, int pageSize, String orderBy) {
		DateTime endTime = new DateTime(endDate).withHourOfDay(23).withMinuteOfHour(59);
		User user = userService.getCurrentUser();
		StringBuilder url = new StringBuilder();
		Sort.Direction orderByDirection = Sort.Direction.fromString(orderBy);
		Sort sorting = new Sort(orderByDirection, sort);
		try {
			Set<Role> roleSet = user.getRoles();
			boolean boss = false;
			for (Role role : roleSet) {
				if (role.getName().equals("BOSS")) {
					boss = true;
				}
			}
			if (pageNumber < 0) {
				pageNumber = 1;
			}
			Page<Order> page;
			if (boss) {
				page = orderService.getOrdersForDashboardBoss(startDate, endTime.toDate(), search, minPrice, maxPrice,
					new PageRequest(pageNumber - 1, pageSize, sorting));
				url.append("directorView/DirectorDashBoard :: tableOrders");
			} else {
				page = orderService.getOrdersForDashboard(user, startDate, endTime.toDate(), search, minPrice, maxPrice,
					new PageRequest(pageNumber - 1, pageSize, sorting));
				url.append("managerView/ManagerDashBoard :: tableOrders");
			}
			model.addAttribute("page", page);
			model.addAttribute("orderList", page.getContent());

			//Pagination variables
			int current = page.getNumber() + 1;
			int begin = 1;
			int end = 1;
			if (current > 5) {
				begin = Math.max(1, current - 5);
				end = Math.min(begin + 5, page.getTotalPages());
			}
			int totalPageCount = page.getTotalPages();
			model.addAttribute("beginIndex", begin);
			model.addAttribute("endIndex", end);
			model.addAttribute("currentIndex", current);
			model.addAttribute("totalPageCount", totalPageCount);
		} catch (Exception e) {
			logger.error("while getting list of orders for Dashboard");
		}
		return url.toString();
	}

	@RequestMapping(value = {"/order/notification/get"}, method = RequestMethod.GET)
	public String getOrdersByNotificationForDashboard(Model model) {
		String user = userService.getCurrentUser().getName();
		List<Notification> myNotes = notificationService.findAllByUser(user);
		List<Order> orderList = new ArrayList<>();
		for (Notification n : myNotes) {
			orderList.add(orderService.get(n.getOrder()));
		}
		model.addAttribute("orderList", orderList);
		return "managerView/ManagerDashBoard :: tableOrders";
	}

	@RequestMapping(value = {"/forgotten"}, method = RequestMethod.GET)
	public ModelAndView forgotten() {
		return new ModelAndView("forgotten");
	}

	@RequestMapping(value = {"/resetpassword/"}, method = RequestMethod.GET)
	public ModelAndView resetpassword(String code, String mail) {
		ModelAndView model = new ModelAndView("resetPassword");
		User user = userService.getByEmail(mail);
		if (user != null & code != null & user.getPassword().equals(code)) {
			String resetPassword = UUID.randomUUID().toString();
			user.setPassword(resetPassword);
			userService.save(user);
			String title = user.getFirstName() + ", Ваш пароль от CaseCRM";
			try {
				mailService.sendEmail(title, "Ваш новый пароль:" + resetPassword, user, "mail/mailPassword");
			} catch (Exception e) {
				logger.error("while sending mail");
			}
			model.addObject("message", "Пароль сброшен, новый пароль выслан вам на почту");
		} else {
			model.addObject("message", "Не удалось сбросить пароль");
		}

		return model;
	}
}
