package main.controller;

import main.model.*;
import main.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
public class DesignerController {

	private final Logger logger = LoggerFactory.getLogger(DesignerController.class);

	private OrderService orderService;

	private ItemService itemService;

	private CommentService commentService;

	private AnswerService answerService;

	private ImageService imageService;

	private UserService userService;

	@Autowired
	public DesignerController(OrderService orderService, ItemService itemService, CommentService commentService,
							  AnswerService answerService, ImageService imageService, UserService userService) {
		this.orderService = orderService;
		this.itemService = itemService;
		this.commentService = commentService;
		this.answerService = answerService;
		this.imageService = imageService;
		this.userService = userService;
	}

	//Designer dashboard
	@RequestMapping(value = {"/designer"}, method = RequestMethod.GET)
	public ModelAndView designer() {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		try {
			model.addObject("orders", orderService.getAllAllowed(userService.getCurrentUser()));
		} catch (Exception e) {
			logger.error("Controller '/designer', orderService.designerOrders() error ");
		}
		return model;
	}

	//Order page
	@RequestMapping(value = {"/designer/order/{id}"}, method = RequestMethod.GET)
	public ModelAndView order(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		try {
			model.addObject("order", orderService.get(id));
		} catch (Exception e) {
			model = new ModelAndView("/designerView/DesignerDashBoard");
			logger.error("Controller '/designer/order', orderId={}", id);
		}
		return model;
	}

	//Item page
	@RequestMapping(value = {"/designer/order/item/{id}"}, method = RequestMethod.GET)
	public ModelAndView item(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		try {
			model.addObject("item", itemService.get(id));
		} catch (Exception e) {
			logger.error("Controller '/designer/order/item', itemId={}", id);
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}

	//Search page DEPRECATED
	@RequestMapping(value = {"/designer/{search}"}, method = RequestMethod.POST)
	public ModelAndView search(@PathVariable("search") String search) {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		try {
			model.addObject("orders", orderService.designFindNumber(search));
		} catch (Exception e) {
			logger.error("Controller '/designer/search', search={}", search);
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}

	//Изменение статуса товара
	@RequestMapping(value = {"/designer/order/item/save/{id}"}, method = RequestMethod.POST)
	public ModelAndView save(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		Item item = itemService.get(id);
		itemService.changeStatus(item.getId());
		itemService.save(item);
		model.addObject("item", item);
		return model;
	}

	//Загрузка файлов
	@RequestMapping(value = "/uploadFile/", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String uploadSampleFiles(@RequestParam(value = "id") Long id, HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		if (imageService.saveBlobImage(multipartRequest, id)) {
			return "Вы удачно загрузили файлы";
		}
		return "Ошибка при загрузке файлов";
	}

	//Смена статуса заказа
	@RequestMapping(value = {"/designer/send/order={id}"}, method = RequestMethod.POST)
	public ModelAndView send(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		try {
			model.addObject("order", orderService.nextStatus(id));
		} catch (Exception e) {
			logger.warn("Вам не удалось сменить статус заказа id={}, статус id={}", id);
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}

	//Добавление комментария
	@RequestMapping(value = {"/designer/order/comment/add={id}"}, method = RequestMethod.POST)
	public ModelAndView addComment(@PathVariable Long id, HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		Comment comment = new Comment(request.getParameter("commentText"), userService.getCurrentUser().toString());
		comment.setTime(new Date());
		commentService.save(comment);
		Order order = orderService.get(id);
		order.getComments().add(comment);
		orderService.save(order);
		model.addObject("order", order);
		model.addObject("tabIndex", 1);
		return model;
	}

	//Добавление ответа на комментарий
	@RequestMapping(value = {"/designer/order/comment/sub/{id}"}, method = RequestMethod.POST)
	public ModelAndView subComment(@PathVariable Long id, HttpServletRequest request) {
		Long commentId = Long.parseLong(request.getParameter("commentBtnOrder"));
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		Comment comment = commentService.get(commentId);
		String content = request.getParameter("commentTextSub");
		Answer answer = new Answer(content, userService.getCurrentUser().toString());
		answer.setTime(new Date());
		answerService.save(answer);
		comment.getAnswers().add(answer);
		commentService.save(comment);
		Order order = orderService.get(id);
		model.addObject("order", order);
		model.addObject("tabIndex", 1);
		return model;
	}

	//Удаление картинки дизайнера
	@RequestMapping(value = {"/designer/order/item/delimage/{id}"}, method = RequestMethod.POST)
	public ModelAndView delImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
		try {
			Image image = imageService.get(id);
			imageService.del(image);
			return new ModelAndView("redirect:/designer/order/item/" + image.getItem().getId());
		} catch (Exception e) {
			logger.error("Ошибка удаления картинки '/designer/order/item', imageId={}", id);
			return new ModelAndView("/designerView/DesignerDashBoard");
		}
	}
}