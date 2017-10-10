package main.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = {"/admin"}, method = RequestMethod.GET)
    public ModelAndView main() {
        LOGGER.warn("Entering in admin panel");

        ModelAndView model = new ModelAndView("admin");
        return model;
    }

}
