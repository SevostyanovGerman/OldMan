package main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DesignerController {

	@RequestMapping(value = {"/designer"}, method = RequestMethod.GET)
	public ModelAndView designer() {
		ModelAndView model = new ModelAndView("/designerView/DesignerDashBoard");
		return model;
	}

	@RequestMapping(value = {"/designer/order"}, method = RequestMethod.GET)
	public ModelAndView order() {
		ModelAndView model = new ModelAndView("/designerView/DesignerOrder");
		return model;
	}


	@RequestMapping(value = {"/designer/order/item"}, method = RequestMethod.GET)
	public ModelAndView item() {
		ModelAndView model = new ModelAndView("/designerView/DesignerItem");
		return model;
	}

}
