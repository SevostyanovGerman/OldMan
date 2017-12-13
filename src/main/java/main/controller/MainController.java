package main.controller;

import main.model.Comment;
import main.model.Helper;
import main.model.Notification;
import main.model.Order;
import main.service.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {

	private final static Logger logger = LoggerFactory.getLogger(MainController.class);

	private CommentService commentService;

	private OrderService orderService;

	private UserService userService;

	private ImageService imageService;

	private NotificationService notificationService;

	@Autowired
	public MainController(CommentService commentService, OrderService orderService,
						  UserService userService, ImageService imageService,
						  NotificationService notificationService) {
		this.commentService = commentService;
		this.orderService = orderService;
		this.userService = userService;
		this.imageService = imageService;
		this.notificationService = notificationService;
	}

	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
							  @RequestParam(value = "logout", required = false) String logout)
		throws ServletException, IOException {
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
		ModelAndView model = new ModelAndView("403");
		return model;
	}

	//Добавление комментария
	@RequestMapping(value = {"/order/comment/add={id}"}, method = RequestMethod.POST)
	public ModelAndView addComment(@PathVariable Long id,
								   @ModelAttribute("commentText") String content,
								   @ModelAttribute("recipient") String recipient,
								   @ModelAttribute("commentBtnOrder") String commentId,
								   HttpServletRequest request) {
		String url = Helper.getUrl(request.getHeader("referer"));
		ModelAndView model = new ModelAndView("redirect:" + url);
		try {
			Comment comment;
			if (!commentId.isEmpty()) {
				Comment parent = commentService.get(Long.parseLong(commentId));
				comment = new Comment(content, userService.getCurrentUser(), recipient, new Date(),
					parent);
			} else {
				comment = new Comment(content, userService.getCurrentUser(), recipient, new Date());
			}
			commentService.save(comment);
			Order order = orderService.get(id);
			order.addComment(comment);
			orderService.save(order);
			Notification notification = new Notification(
				recipient.equals("") ? comment.getParent().getCreatedBy().getName() : recipient,
				order.getId());
			notificationService.save(notification);
			model.addObject("order", order);
			model.addObject("tabIndex", 1);
		} catch (Exception e) {
			logger.error("Ошибка при создании комментария, заказ id={}", e);
		}
		return model;
	}

	@RequestMapping(value = "/orders/search", method = RequestMethod.POST)
	public String orderSearch(String search, Date startDate, Date endDate, Model model, String sort,
							  Double minPrice, Double maxPrice, int pageNumber, int pageSize,
							  HttpServletRequest request) {
		String url = Helper.getUrl(request.getHeader("referer"));

		try {

			if (url.contains("manager")) {
				url = "managerView/ManagerDashBoard :: tableOrders";
			} else {
				url = "directorView/DirectorDashBoard :: tableOrders";
			}

			DateTime end2 = new DateTime(endDate).withHourOfDay(23).withMinuteOfHour(59);
			List<Order> orderList = orderService
				.getOrdersForDashboard(userService.getCurrentUser(), startDate, end2.toDate(),
					search, minPrice, maxPrice);

			if (pageNumber < 0) {
				pageNumber = 1;
			}
			PagedListHolder page = new PagedListHolder(orderList);
			page.setPageSize(pageSize); // number of items per page
			orderService.sorting(page.getSource(), sort);
			page.setPage(pageNumber - 1); // set to first page

			model.addAttribute("page", page);
			model.addAttribute("orderList", page.getPageList());

			//Pagination variables
			int current = page.getPage() + 1;
			int begin = Math.max(1, current - 5);
			int end = Math.min(begin + 5, page.getPageCount());
			int totalPageCount = page.getPageCount();

			model.addAttribute("beginIndex", begin);
			model.addAttribute("endIndex", end);
			model.addAttribute("currentIndex", current);
			model.addAttribute("totalPageCount", totalPageCount);
		} catch (Exception e) {
			logger.error("while getting list of orders");
		}

		return url;
	}

}
