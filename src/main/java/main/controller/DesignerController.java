package main.controller;

import main.model.Image;
import main.model.Item;
import main.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;

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
		try {
			Item item = itemService.get(id);
			itemService.changeStatus(item.getId());
			itemService.save(item);
			model.addObject("item", item);
		} catch (Exception e) {
			logger.error("Ошибка при изменении статуса заказ id={}");
			return new ModelAndView("redirect:/designer/order/" + id);
		}
		return model;
	}

	//Смена статуса заказа
	@RequestMapping(value = {"/designer/send/order={id}"}, method = RequestMethod.POST)
	public ModelAndView send(@PathVariable Long id) {
		try {
			orderService.nextStatus(id);
		} catch (Exception e) {
			logger.warn("Вам не удалось сменить статус заказа id={}, статус id={}", id);
			return new ModelAndView("redirect:/designer/");
		}
		return new ModelAndView("redirect:/designer/");
	}

	//Удаление картинки дизайнера
	@RequestMapping(value = {"/designer/order/item/delimage/{id}"}, method = RequestMethod.POST)
	public ModelAndView delImage(@PathVariable("id") Long id) throws IOException {
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