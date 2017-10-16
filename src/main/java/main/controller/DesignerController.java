package main.controller;

import main.model.Item;
import main.service.ItemService;
import main.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
public class DesignerController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ItemService itemService;

	@RequestMapping(value = {"/designer"}, method = RequestMethod.GET)
	public ModelAndView designer() {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		model.addObject("orders", orderService.designerOrders());
		return model;
	}

	@RequestMapping(value = {"/designer/order"}, method = RequestMethod.GET)
	public ModelAndView order(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		String id = request.getParameter("orderId");
		model.addObject("order", orderService.get(Long.parseLong(id)));
		return model;
	}

	@RequestMapping(value = {"/designer/order/item"}, method = RequestMethod.GET)
	public ModelAndView item(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		model.addObject("item", itemService.get(Long.parseLong(request.getParameter("itemId"))));
		return model;
	}

	@RequestMapping(value = {"/designer/search"}, method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		String search = request.getParameter("search");
		model.addObject("orders", orderService.designFindNumber(search));
		return model;
	}

	@RequestMapping(value = {"/designer/order/item/save/{id}"}, method = RequestMethod.POST)
	public ModelAndView save(@PathVariable Long id, HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		String status = request.getParameter("status");
		Item item = itemService.get(id);

		if (status != null) {
			item.setStatus(status);
			itemService.save(item);
		}
		model.addObject("item", item);
		return model;
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String uploadSampleFiles(@RequestParam(value = "file") MultipartFile file) {

		String name = "drop";
		if (!file.isEmpty()) {
			name = file.getOriginalFilename();

			try {
				byte[] bytes = file.getBytes();
				BufferedOutputStream stream =
						new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
				stream.write(bytes);
				stream.close();
				return "Вы удачно загрузили " + name + " в " + name + "-uploaded !";
			} catch (Exception e) {
				return "Вам не удалось загрузить " + name + " => " + e.getMessage();
			}
		} else {
			return "Вам не удалось загрузить " + name + " потому что файл пустой.";
		}
	}
}