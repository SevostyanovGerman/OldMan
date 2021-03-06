package main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import main.Helpers;
import main.model.Comment;
import main.model.Notification;
import main.model.Order;
import main.model.Role;
import main.model.User;
import main.service.CommentService;
import main.service.MailService;
import main.service.NotificationService;
import main.service.OrderService;
import main.service.UserService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController implements ErrorController {

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
				model.addObject("authors", Helpers.authors());
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
		@ModelAttribute("recipient") String recipient, @ModelAttribute("commentBtnOrder") String commentId,
		HttpServletRequest request) {
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
			Comment newComment = commentService.save(comment);
			Order order = orderService.get(id);
			order.addComment(comment);
			orderService.save(order);
			Notification notification = new Notification(
				recipient.equals("") ? comment.getParent().getCreatedBy().getName() : recipient, order.getId(),
				newComment.getId());
			notificationService.save(notification);
			model.addObject("order", order);
			model.addObject("tabIndex", 1);
			new Thread(() -> mailService.sendNotificationByMail(userService.getByName(recipient), comment)).start();
		} catch (Exception e) {
			logger.error("While adding comment for order id={}", id);
		}
		return model;
	}

	@RequestMapping(value = {"/order/comment/delete={id}"}, method = RequestMethod.GET)
	public ModelAndView deleteComment(@PathVariable Long id, HttpServletRequest request) {
		String url = Helpers.getUrl(request.getHeader("referer"));
		ModelAndView model = new ModelAndView("redirect:" + url);
		Comment comment = commentService.get(id);
		commentService.delete(comment);
		notificationService.removeAllByCommentId(id);
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
		Double maxPrice, int pageNumber, int pageSize, String orderBy, HttpSession session) {
		DateTime endTime = new DateTime(endDate).withHourOfDay(23).withMinuteOfHour(59);
		User user = userService.getCurrentUser();
		StringBuilder url = new StringBuilder();
		Sort.Direction orderByDirection = Sort.Direction.fromString(orderBy);
		Sort sorting = new Sort(orderByDirection, sort);
		session.setAttribute("search", search);
		session.setAttribute("startDate", startDate);
		session.setAttribute("endDate", endDate);
		session.setAttribute("maxPrice", maxPrice);
		session.setAttribute("minPrice", minPrice);
		session.setAttribute("sort", sort);
		session.setAttribute("pageSize", pageSize);
		session.setAttribute("orderBy", orderBy);
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
			session.setAttribute("pageNumber", pageNumber - 1);
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
		try {
			ModelAndView model = new ModelAndView("resetPassword");
			User user = userService.getByEmail(mail);
			if (user.getToken() != null & code != null & user.getToken().equals(code)) {
				if (user.getTokenExpire().before(new Date())) {
					model.addObject("message", "срок действия токена истек");
					return model;
				}
				model.addObject("message", code);
				return model;
			}
			model.addObject("message", "Не удалось сбросить пароль");
			return model;
		} catch (Exception e) {
			return new ModelAndView("redirect:/");
		}

	}

	@RequestMapping("/error")
	public ModelAndView error(HttpServletRequest request) {

		return new ModelAndView("redirect:/");
	}

	@RequestMapping("/")
	public ModelAndView index(HttpServletRequest request) {
		try {
			String url = "/login";
			User user = userService.getCurrentUser();

			Set<Role> roleList = user.getRoles();
			if (!roleList.isEmpty()) {
				for (Role role : roleList) {
					url = role.getUrl();
					break;
				}
			}
			return new ModelAndView("redirect:" + url);
		} catch (Exception e) {
			return new ModelAndView("redirect:/login");
		}
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
