package main.controller;

import main.model.*;
import main.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;

@Controller
public class DesignerController {

	private final Logger logger = LoggerFactory.getLogger(DesignerController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private UserService userService;

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

	@RequestMapping(value = {"/designer/order"}, method = RequestMethod.GET)
	public ModelAndView order(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		try {
			String id = request.getParameter("orderId");
			model.addObject("order", orderService.get(Long.parseLong(id)));
		} catch (Exception e) {
			model = new ModelAndView("/designerView/DesignerDashBoard");
			logger.error("Controller '/designer/order', orderId={}", request.getParameter("orderId"));
		}
		return model;
	}

	@RequestMapping(value = {"/designer/order/item"}, method = RequestMethod.GET)
	public ModelAndView item(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		try {
			model.addObject("item", itemService.get(Long.parseLong(request.getParameter("itemId"))));
		} catch (Exception e) {
			logger.error("Controller '/designer/order/item', itemId={}", request.getParameter("itemId"));
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}

	@RequestMapping(value = {"/designer/search"}, method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		try {
			String search = request.getParameter("search");
			model.addObject("orders", orderService.designFindNumber(search));
		} catch (Exception e) {
			logger.error("Controller '/designer/search', search={}", request.getParameter("search"));
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}

//	@RequestMapping(value = {"/designer/order/item/save/{id}"}, method = RequestMethod.POST)
//	public ModelAndView save(@PathVariable Long id, HttpServletRequest request) {
//		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
//		String status = request.getParameter("status");
//		try {
//			Item item = itemService.get(id);
//			if (status != null) {
//				item.setStatus(status);
//				itemService.save(item);
//			}
//			model.addObject("item", item);
//		} catch (Exception e) {
//			logger.error("Controller '/designer/order/item/save/', id={}", id);
//			model = new ModelAndView("/designerView/DesignerDashBoard");
//		}
//		return model;
//	}

	@RequestMapping(value = {"/designer/order/item/save/{id}"}, method = RequestMethod.POST)
	public ModelAndView save(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		Item item = itemService.get(id);
		itemService.changeStatus(item.getId());
		itemService.save(item);
		model.addObject("item", item);
		return model;
	}

	@RequestMapping(value = "/uploadFile/", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String uploadSampleFiles(@RequestParam(value = "id") Long id,
									@RequestParam(value = "file") MultipartFile file) {
		String name = "drop";
		Item item = itemService.get(id);
		if (!file.isEmpty()) {
			name = file.getOriginalFilename();
			try {
				byte[] bytes = file.getBytes();
				Image image = new Image();
				Blob blob = new SerialBlob(bytes);
				image.setImage(blob);
				imageService.save(image);
				item.getImages().add(image);
				itemService.save(item);
				logger.info("Вы удачно загрузили файл {}", name);
				return "Вы удачно загрузили " + name + " в " + name + "-uploaded !";
			} catch (Exception e) {
				logger.warn("Вам не удалось загрузить  {}" + e.getMessage(), name);
				return "Вам не удалось загрузить " + name + " => " + e.getMessage();
			}
		} else {
			logger.warn("Вам не удалось загрузить  {} потому что файл пустой.", name);
			return "Вам не удалось загрузить " + name + " потому что файл пустой.";
		}
	}

	@RequestMapping(value = {"/designer/send/order={id}"}, method = RequestMethod.POST)
	public ModelAndView send(@PathVariable Long id) {
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		try {
			model.addObject("order", orderService.nextStatus(id));
		} catch (Exception e) {
			logger.warn("Вам не удалось сменить статус заказа id={}, статус id={}", id);
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}

	@RequestMapping(value = {"/designer/order/comment/add={id}"}, method = RequestMethod.POST)
	public ModelAndView addComment(@PathVariable Long id, HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		Comment comment = new Comment(request.getParameter("commentText"), userService.getCurrentUser().toString());
		commentService.save(comment);
		Order order = orderService.get(id);
		order.getComments().add(comment);
		orderService.save(order);
		model.addObject("order", order);
		model.addObject("tabIndex", 1);
		return model;
	}

	@RequestMapping(value = {"/designer/order/comment/sub/{id}"}, method = RequestMethod.POST)
	public ModelAndView subComment(@PathVariable Long id, HttpServletRequest request) {
		Long commentId = Long.parseLong(request.getParameter("commentBtnOrder"));
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		Comment comment = commentService.get(commentId);
		String content = request.getParameter("commentTextSub");
		Answer answer = new Answer(content, userService.getCurrentUser().toString());
		answerService.save(answer);
		comment.getAnswers().add(answer);
		commentService.save(comment);
		Order order = orderService.get(id);
		model.addObject("order", order);
		model.addObject("tabIndex", 1);
		return model;
	}

	@RequestMapping(value = {"/designer/order/item/delimage/{id}"}, method = RequestMethod.POST)
	public ModelAndView delImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		try {
			Image image = imageService.get(id);
			imageService.del(image);
			model.addObject("item", image.getItem());
			response.sendRedirect("/designer/order/item/?itemId=" + image.getItem().getId());
		} catch (Exception e) {
			logger.error("Ошибка удаления картинки '/designer/order/item', imageId={}", id);
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}
}