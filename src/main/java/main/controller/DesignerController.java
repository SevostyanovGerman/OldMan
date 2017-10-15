package main.controller;

import main.model.Item;
import main.service.ItemService;
import main.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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

	@RequestMapping(value = {"/designer/order/item"}, method = RequestMethod.POST)
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

}
