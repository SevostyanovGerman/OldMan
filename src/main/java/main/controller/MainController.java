package main.controller;


import main.model.Order;
import main.model.User;
import main.service.OrderService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView main() {

        Order order = orderService.get(1l);

        ModelAndView model = new ModelAndView("main");
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByName(name);
        model.addObject("user",user);

        List<User> list =userService.getByRole("ADMIN");
        model.addObject("roles",userService.getByRole("ADMIN"));
        return model;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
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
        }
        catch (Exception e) {

            return null;
        }
    }


    @RequestMapping(value = {"/403"}, method = RequestMethod.GET)
    public ModelAndView page403() {
        ModelAndView model = new ModelAndView("403");
        return model;
    }

    @RequestMapping(value = {"/manager"}, method = RequestMethod.GET)
    public ModelAndView manager() {
        ModelAndView model = new ModelAndView("ManagerDashBoard");
        model.addObject("orders", orderService.getAll());
        model.addObject("items", orderService.get(1l).getItems());
        return model;
    }

}
