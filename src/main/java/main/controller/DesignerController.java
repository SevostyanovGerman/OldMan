package main.controller;

import main.model.Item;
import main.service.ItemService;
import main.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
public class DesignerController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ItemService itemService;

	private final Logger logger = LoggerFactory.getLogger(DesignerController.class);

	@RequestMapping(value = {"/designer"}, method = RequestMethod.GET)
	public ModelAndView designer() {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		try{
			model.addObject("orders", orderService.designerOrders());
		}
		catch (Exception e){
			logger.error("Controller '/designer', orderService.designerOrders() error ");
		}
		return model;
	}

	@RequestMapping(value = {"/designer/order"}, method = RequestMethod.GET)
	public ModelAndView order(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		try{
			String id = request.getParameter("orderId");
			model.addObject("order", orderService.get(Long.parseLong(id)));
		} catch (Exception e){
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
		}
		catch (Exception e) {
			logger.error("Controller '/designer/order/item', itemId={}", request.getParameter("itemId"));
			 model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}

	@RequestMapping(value = {"/designer/search"}, method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		try{
			String search = request.getParameter("search");
			model.addObject("orders", orderService.designFindNumber(search));
		}
		catch (Exception e){
			logger.error("Controller '/designer/search', search={}", request.getParameter("search"));
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
		return model;
	}

	@RequestMapping(value = {"/designer/order/item/save/{id}"}, method = RequestMethod.POST)
	public ModelAndView save(@PathVariable Long id, HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		String status = request.getParameter("status");
		try{
			Item item = itemService.get(id);
			if (status != null) {
				item.setStatus(status);
				itemService.save(item);
			}
			model.addObject("item", item);
		}
		catch (Exception e) {
			logger.error("Controller '/designer/order/item/save/', id={}", id);
			model = new ModelAndView("/designerView/DesignerDashBoard");
		}
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
				logger.info("Вы удачно загрузили файл {}",name);
				return "Вы удачно загрузили " + name + " в " + name + "-uploaded !";
			} catch (Exception e) {
				logger.warn("Вам не удалось загрузить  {}"+ e.getMessage(),name);
				return "Вам не удалось загрузить " + name + " => " + e.getMessage();
			}
		} else {
			logger.warn("Вам не удалось загрузить  {} потому что файл пустой.",name);
			return "Вам не удалось загрузить " + name + " потому что файл пустой.";
		}
	}
}