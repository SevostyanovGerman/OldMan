package main.controller;

import main.model.Comment;
import main.model.Order;
import main.service.CommentService;
import main.service.ImageService;
import main.service.OrderService;
import main.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Controller
public class MainController {

	private final Logger logger = LoggerFactory.getLogger(MainController.class);

	private CommentService commentService;

	private OrderService orderService;

	private UserService userService;

	private ImageService imageService;

	@Autowired
	public MainController(CommentService commentService, OrderService orderService, UserService userService,
						  ImageService imageService) {
		this.commentService = commentService;
		this.orderService = orderService;
		this.userService = userService;
		this.imageService = imageService;
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
	public ModelAndView addComment(@PathVariable Long id, @ModelAttribute("commentText") String content,
								   HttpServletRequest request) {
		String referer = request.getHeader("referer");
		String url = getUrl(referer);
		ModelAndView model = new ModelAndView("redirect:" + url);
		try {
			Comment comment = new Comment(content, userService.getCurrentUser().toString(), new Date());
			commentService.save(comment);
			Order order = orderService.get(id);
			order.getComments().add(comment);
			orderService.save(order);
			model.addObject("order", order);
			model.addObject("tabIndex", 1);
		} catch (Exception e) {
			logger.error("Ошибка при создании комментария, заказ id={}");
			return model;
		}
		return model;
	}

	//Добавление ответа на комментарий
	@RequestMapping(value = {"/order/comment/sub/{id}"}, method = RequestMethod.POST)
	public ModelAndView subComment(@PathVariable Long id, @ModelAttribute("commentBtnOrder") Long commentId,
								   @ModelAttribute("commentTextSub") String content, HttpServletRequest request) {
		String referer = request.getHeader("referer");
		String url = getUrl(referer);
		ModelAndView model = new ModelAndView("redirect:" + url);
		try {
			Comment comment = commentService.get(commentId);
			Comment answer = new Comment(content, userService.getCurrentUser().toString(), new Date());
			commentService.save(answer);
			comment.getAnswers().add(answer);
			commentService.save(comment);
			Order order = orderService.get(id);
			model.addObject("order", order);
			model.addObject("tabIndex", 1);
		} catch (Exception e) {
			logger.error("Ошибка создании ответа на комментарий  коментарий id={}", commentId);
			return model;
		}
		return model;
	}

	private String getUrl(String referer) {
		String url;
		int index = referer.indexOf("?");
		if (index > 0) {
			url = referer.substring(0, index);
		} else {
			url = referer.toString();
		}
		return url;
	}
}
