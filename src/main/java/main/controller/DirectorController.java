package main.controller;

import main.model.*;
import main.service.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DirectorController {

	private UserService userService;

	private OrderService orderService;

	private StatusService statusService;

	private RoleService roleService;

	private CustomerService customerService;

	private DeliveryService deliveryService;

	private NotificationService notificationService;

	private ProductService productService;

	private PhoneModelService phoneModelService;

	private final static Logger logger = LoggerFactory.getLogger(DirectorController.class);

	@Autowired
	public DirectorController(UserService userService, OrderService orderService,
		StatusService statusService, RoleService roleService, CustomerService customerService,
		DeliveryService deliveryService, NotificationService notificationService,
		ProductService productService, PhoneModelService phoneModelService) {
		this.userService = userService;
		this.orderService = orderService;
		this.statusService = statusService;
		this.roleService = roleService;
		this.customerService = customerService;
		this.deliveryService = deliveryService;
		this.notificationService = notificationService;
		this.productService = productService;
		this.phoneModelService = phoneModelService;
	}

	@RequestMapping(value = {"/director"}, method = RequestMethod.GET)
	public ModelAndView director() {
		ModelAndView modelAndView = new ModelAndView("/directorView/DirectorDashBoard");
		DateTime startDate = new DateTime().withDayOfMonth(1);
		DateTime endDate = new DateTime().withHourOfDay(23).withMinuteOfHour(59);
		Sort.Direction orderByDirection = Sort.Direction.fromString("DESC");
		Sort sorting = new Sort(orderByDirection, "number");

		Page page = orderService
			.getOrdersForDashboardBoss(startDate.toDate(), endDate.toDate(), null, null, null,
				new PageRequest(0, 25, sorting));
		modelAndView.addObject("orderList", page);

		//Pagination variables
		int current = page.getNumber() + 1;
		int begin = 1;
		int end = 1;
		if (current > 5) {
			begin = Math.max(1, current - 5);
			end = Math.min(begin + 5, page.getTotalPages());
		}
		int totalPageCount = page.getTotalPages();
		modelAndView.addObject("page", page);
		modelAndView.addObject("beginIndex", begin);
		modelAndView.addObject("endIndex", end);
		modelAndView.addObject("currentIndex", current);
		modelAndView.addObject("totalPageCount", totalPageCount);
		modelAndView.addObject("allStatus", statusService.getAll());
		return modelAndView;
	}

	@RequestMapping(value = {"/director/stuff"}, method = RequestMethod.GET)
	public ModelAndView showStaff(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/directorView/DirectorStuffBoard");
		injectMessageToPage(request, model);
		try {
			model.addObject("stuff", userService.getAllUsers());
		} catch (Exception e) {
			logger.error("Can\'t get stuff list", e);
		}
		return model;
	}

	//---------------------- Status block ---------------------
	/*
	 * Пока что не используется
	 */
	@RequestMapping(value = {"/director/controlpanel"}, method = RequestMethod.GET)
	public ModelAndView controlPanel() {
		ModelAndView model = new ModelAndView("/directorView/ControlPanel");
		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/statuses"}, method = RequestMethod.GET)
	public ModelAndView controlPanelStatus(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/directorView/ControlPanelStatus");

		/*
		 * Вначале извлекаем информацию о успешности выполнения предыдущих операциях,
		 * если таковые имели место. Если есть, то передаём их в model чтобы отобразить на странице.
		 */
		injectMessageToPage(request, model);
		try {
			model.addObject("statuses", statusService.getAll());
			model.addObject("newstatus", new Status());
		} catch (Exception e) {
			logger.error("Can\'t getById status list", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/statuses/create"}, method = RequestMethod.POST)
	public ModelAndView createStatus(@ModelAttribute("newstatus") @Valid Status newstatus,
		BindingResult bindingResult, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();

		/*
		 * Ищем в базе статус с таким же именем.
		 * В случае нахождения статуса с таким именем генерим сообщение error.
		 * Если же ничего не находим то записываем статус в базу данных и создаём сообщение о усрехе.
		 */
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("/directorView/ControlPanelStatus");
			modelAndView.addObject("statuses", statusService.getAll());
			return modelAndView;
		} else {
			String searchingStatus = newstatus.getName();
			long searchingIndex = newstatus.getNumber();
			Status foundStatus = statusService.getByName(searchingStatus);
			Status foundStatusByNumber = statusService.get(searchingIndex);
			if (foundStatus != null) {
				String error = "Статус с именем: " + searchingStatus + " уже существует";
				request.getSession().setAttribute("error", error);
			} else if ((foundStatusByNumber != null) && (searchingIndex != 0)) {
				String error = "Статус с индексом: " + searchingIndex + " уже существует";
				request.getSession().setAttribute("error", error);
			} else {
				try {
					statusService.save(newstatus);
					String success = "Статус с именем: " + searchingStatus + " успешно создан";
					request.getSession().setAttribute("success", success);
				} catch (Exception e) {
					logger.error("Can\'t create status", e);
					String error = "Ошибка при записи в базу данных";
					request.getSession().setAttribute("error", error);
				}
			}
		}
		modelAndView.setViewName("redirect:/director/controlpanel/statuses");
		return modelAndView;
	}

	@RequestMapping(value = {"/director/controlpanel/statuses/update"}, method = RequestMethod.POST)
	public ModelAndView updateStatus(@ModelAttribute("status") @Valid Status incomingStatus,
		BindingResult bindingResult, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();

		/*
		 * Ищем в базе статус с таким же именем.
		 * В случае нахождения статуса с таким именем, проверяем id и в случае если они разные генерим сообщение error.
		 * Если же ничего не находим или если id совпадают, то обновляем статус в базе данных и создаём сообщение об
		 * успехе.
		 */
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("/directorView/ControlPanelStatus");
			modelAndView.addObject("wrongstatus", incomingStatus);
			modelAndView.addObject("newstatus", new Status());
			modelAndView.addObject("statuses", statusService.getAll());
			return modelAndView;
		} else {
			long number = incomingStatus.getNumber();
			Status foundStatus = statusService.getByName(incomingStatus.getName());
			Status foundStatusByNumber = statusService.get(number);
			if ((foundStatus != null) && (incomingStatus.getId() != foundStatus.getId())) {
				String error = "Статус с именем: " + incomingStatus.getName() + " уже существует";
				request.getSession().setAttribute("error", error);
			} else if ((foundStatusByNumber != null) && (number > 0) && (incomingStatus.getId()
				!= foundStatusByNumber.getId())) {
				String error = "Статус с индексом: " + number
					+ " уже существует. Допустимо дублирование только с индексом: 0";
				request.getSession().setAttribute("error", error);
			} else {
				try {
					statusService.save(incomingStatus);
					String success = "Статус успешно изменён.";
					request.getSession().setAttribute("success", success);
				} catch (Exception e) {
					logger.error("Can\'t save status", e);
					String error = "Ошибка при записи в базу данных";
					request.getSession().setAttribute("error", error);
				}
			}
		}
		modelAndView.setViewName("redirect:/director/controlpanel/statuses");
		return modelAndView;
	}

	@RequestMapping(value = {
		"/director/controlpanel/status/delete/{id}"}, method = RequestMethod.GET)
	public String deleteStatus(@PathVariable("id") Long id, HttpServletRequest request) {
		logger.info("Deleting status with id: ", id);

		/*
		 * Перед удалением получаем статус из базы данных и устанавливаем значение поля
		 * Deleted в true. По умолчание у всех установлено значение false. Далее данная
		 * сущность не отображается.
		 */
		Status deletedStatus = null;
		try {
			deletedStatus = statusService.getById(id);
		} catch (Exception e) {
			logger.error("Can\'t getById status with id: ", id);
			String error = "Ошибка при запросе статуса c id: " + id + " из базы данных";
			request.getSession().setAttribute("error", error);
		}
		if (deletedStatus != null) {
			deletedStatus.setDeleted(true);
			try {
				statusService.save(deletedStatus);
				String success = "Статус с id:" + id + " и именем: " + deletedStatus.getName()
					+ " успешно удалён";
				request.getSession().setAttribute("success", success);
			} catch (Exception e) {
				logger.error("Can\'t delete status with id: ", id);
				String error = "Ошибка при удалении статуса c id: " + id + " из базы данных";
				request.getSession().setAttribute("error", error);
			}
		}
		return "redirect:/director/controlpanel/statuses";
	}

	//---------------------- Role block ---------------------
	@RequestMapping(value = {"/director/controlpanel/roles"}, method = RequestMethod.GET)
	public ModelAndView controlPanelRole(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/directorView/ControlPanelRole");

		/*
		 * Вначале извлекаем информацию о успешности выполнения предыдущих операциях,
		 * если таковые имели место. Если есть, то передаём их в model чтобы отобразить на странице.
		 */
		injectMessageToPage(request, model);
		try {
			model.addObject("roles", roleService.getAll());
			model.addObject("allStatuses", statusService.getAll());
			model.addObject("role", new Role());
		} catch (Exception e) {
			logger.error("Can\'t get role list", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/roles/create"}, method = RequestMethod.POST)
	public String createRole(@ModelAttribute("role") Role incomingRole,
		HttpServletRequest request) {

		/*
		 * Ищем в базе должность с таким же именем.
		 * В случае нахождения должности с таким именем генерим сообщение error.
		 * Если же ничего не находим то записываем должность в базу данных и создаём сообщение о усрехе.
		 */
		String searchingRole = incomingRole.getName();
		Role foundRole = roleService.getByName(searchingRole);
		if (foundRole != null) {
			String error = "Должность с именем: " + searchingRole + " уже существует";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				roleService.save(incomingRole);
			} catch (Exception e) {
				logger.error("Can\'t save new role", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}
			String success = "Должность с именем: " + searchingRole + " успешно создана";
			request.getSession().setAttribute("success", success);
		}
		return "redirect:/director/controlpanel/roles";
	}

	@RequestMapping(value = {"/director/controlpanel/roles/update"}, method = RequestMethod.POST)
	public String updateRole(@ModelAttribute("role") Role incomingRole,
		HttpServletRequest request) {

		/*
		 * Ищем в базе должность с таким же именем.
		 * В случае нахождения должности с таким именем, проверяем id и в случае если они разные генерим сообщение
		 * error.
		 * Если же ничего не находим или если id совпадают, то обновляем должость в базе данных и создаём сообщение о
		 * усрехе.
		 */
		Role foundRole = roleService.getByName(incomingRole.getName());
		if ((foundRole != null) && (incomingRole.getId() != foundRole.getId())) {
			String error = "Должность с именем: " + incomingRole.getName() + " уже существует";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				roleService.save(incomingRole);
			} catch (Exception e) {
				logger.error("Can\'t update role", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}
			String success = "Должность  успешно изменёна.";
			request.getSession().setAttribute("success", success);
		}
		return "redirect:/director/controlpanel/roles";
	}

	@RequestMapping(value = {"/director/controlpanel/role/edit/{id}"}, method = RequestMethod.GET)
	public ModelAndView editRole(@PathVariable("id") Long id, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();

		/*
		 * Ищем в базе должность с таким id.
		 * В случае нахождения роли с таким id, добавляем его в model.
		 * Если же ничего не находим, то создаём сообщение о ошибке.
		 */
		Role role = roleService.get(id);
		if (role == null) {
			String error = "Должность с id: " + id + " не найдена";
			request.getSession().setAttribute("error", error);
			model.setViewName("redirect:/director/controlpanel/roles");
		} else {
			model.setViewName("/directorView/ControlPanelRoleEdit");
			model.addObject("roles", roleService.getAll());
			model.addObject("allStatuses", statusService.getAll());
			model.addObject("role", role);
		}
		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/role/delete/{id}"}, method = RequestMethod.GET)
	public String deleteRole(@PathVariable("id") Long id, HttpServletRequest request) {
		logger.info("Deleting role with id: ", id);

		/*
		 * Перед удалением получаем должность из базы данных и устанавливаем значение поля
		 * Deleted в true. По умолчание у всех установлено значение false. Далее данная
		 * сущность не отображается.
		 */
		Role deletedRole = null;
		try {
			deletedRole = roleService.get(id);
		} catch (Exception e) {
			logger.error("Can\'t getById role with id: ", id);
			String error = "Ошибка при запросе должности c id: " + id + " из базы данных";
			request.getSession().setAttribute("error", error);
		}
		if (deletedRole != null) {
			deletedRole.setDeleted(true);
			try {
				roleService.save(deletedRole);
				String success = "Должность с id:" + id + " и именем: " + deletedRole.getName()
					+ " успешно удалёна";
				request.getSession().setAttribute("success", success);
			} catch (Exception e) {
				logger.error("Can\'t delete role with id: ", id);
				String error = "Ошибка при удалении должности c id: " + id + " из базы данных";
				request.getSession().setAttribute("error", error);
			}
		}
		return "redirect:/director/controlpanel/roles";
	}

	//---------------------- User block ---------------------
	@RequestMapping(value = {"/director/controlpanel/user"}, method = RequestMethod.GET)
	public ModelAndView controlPanelUser(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/directorView/ControlPanelUser");
		/*
		 * Вначале извлекаем информацию о успешности выполнения предыдущих операциях,
		 * если таковые имели место. Если есть, то передаём их в model чтобы отобразить на странице.
		 */
		injectMessageToPage(request, model);
		try {
			model.addObject("allRoles", roleService.getAll());
			model.addObject("user", new User());
		} catch (Exception e) {
			logger.error("Can\'t get user list", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/user/create"}, method = RequestMethod.POST)
	public ModelAndView createUser(@ModelAttribute("user") @Valid User incomingUser,
		BindingResult bindingResult, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();

		/*
		 * Ищем в базе роль с таким же именем.
		 * В случае нахождения роли с таким именем генерим сообщение error.
		 * Если же ничего не находим то записываем роль в базу данных и создаём сообщение о усрехе.
		 */

		if (bindingResult.hasErrors()) {
			model.setViewName("/directorView/ControlPanelUser");
			model.addObject("allRoles", roleService.getAll());
			return model;
		} else {
			String searchingUser = incomingUser.getName();
			String searchingEmail = incomingUser.getEmail();
			User foundUser = userService.getByName(searchingUser);
			User foundUserByEmail = userService.getByEmail(searchingEmail);
			if (foundUser != null) { //проверяем есть ли сотрудник с таким логином
				String error = "Пользователь с логином: " + searchingUser + " уже существует";
				request.getSession().setAttribute("error", error);
			} else if (foundUserByEmail != null) { //проверяем есть ли сотрудник с такой почтой
				String error = "Сотрудник с таким email уже существует";
				request.getSession().setAttribute("error", error);
			} else {
				try {
					incomingUser.setCreated(new Date());
					userService.setPasswordEncoder(incomingUser, incomingUser.getPassword());
					userService.save(incomingUser);
					String success = "Пользователь с логином: " + searchingUser + " успешно создан";
					request.getSession().setAttribute("success", success);
				} catch (Exception e) {
					logger.error("Can\'t save user", e);
					String error = "Ошибка при записи в базу данных";
					request.getSession().setAttribute("error", error);
				}
			}
		}
		model.setViewName("redirect:/director/controlpanel/user");
		return model;

	}

	@RequestMapping(value = {"/director/controlpanel/user/update"}, method = RequestMethod.POST)
	public ModelAndView updateUser(@ModelAttribute("user") @Valid User incomingUser,
		BindingResult bindingResult, HttpServletRequest request) {

		ModelAndView model = new ModelAndView();
		/*
		 * Ищем в базе пользователя с таким же логином.
		 * В случае нахождения пользователя с таким логином, проверяем id и в случае если они разные генерим сообщение
		 * error.
		 * Если же ничего не находим или если id совпадают, то обновляем пользователя в базе данных и создаём
		 * сообщение о усрехе.
		 */
		if (bindingResult.hasErrors()) {
			model.setViewName("/directorView/ControlPanelUserEdit");
			model.addObject("allRoles", roleService.getAll());
			model.addObject("allStatuses", statusService.getAll());
			return model;
		} else {
			User foundUserByName = userService.getByName(incomingUser.getName());
			User foundUserByEmail = userService.getByName(incomingUser.getEmail());
			if ((foundUserByName != null) && (incomingUser.getId() != foundUserByName.getId())) {
				String error =
					"Пользователь с логином: " + incomingUser.getName() + " уже существует";
				request.getSession().setAttribute("error", error);
			} else if ((foundUserByEmail != null) && (incomingUser.getId() != foundUserByEmail
				.getId())) {
				String error =
					"Пользователь с почтой: " + incomingUser.getEmail() + " уже существует";
				request.getSession().setAttribute("error", error);
			} else {
				try {
					User checkPasswordUser = userService.get(incomingUser.getId());
					if (!checkPasswordUser.getPassword().equals(incomingUser.getPassword())) {
						userService.setPasswordEncoder(incomingUser, incomingUser.getPassword());
					}
					userService.save(incomingUser);
					String success = "Информация о пользователе успешно записана в базе данных.";
					request.getSession().setAttribute("success", success);
				} catch (Exception e) {
					logger.error("Can\'t update user", e);
					String error = "Ошибка при записи в базу данных";
					request.getSession().setAttribute("error", error);
				}
			}
		}
		model.setViewName("redirect:/director/stuff");
		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/user/edit/{id}"}, method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable("id") Long id, HttpServletRequest request) {
		logger.info("Edit user with id: ", id);
		ModelAndView model = new ModelAndView();
		User user = null;
		try {
			user = userService.get(id);
		} catch (Exception e) {
			logger.error("Can\'t get user with id: ", id);
			String error = "Ошибка при обращении к базе данных";
			request.getSession().setAttribute("error", error);
			model.setViewName("redirect:/director/stuff");
			return model;
		}
		if (user != null) {
			model.setViewName("/directorView/ControlPanelUserEdit");
			model.addObject("allRoles", roleService.getAll());
			model.addObject("allStatuses", statusService.getAll());
			model.addObject("user", user);
		} else {
			String error = "Пользователь с id: " + id + " не найден";
			request.getSession().setAttribute("error", error);
			model.setViewName("redirect:/director/stuff");
		}
		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/user/delete/{id}"}, method = RequestMethod.GET)
	public String deleteUser(@PathVariable("id") Long id, HttpServletRequest request) {
		logger.info("Deleting user with id: ", id);

		/*
		 * Перед удалением получаем пользователя из базы данных и устанавливаем значение поля
		 * Deleted в true. По умолчание у всех установлено значение false. Далее данная
		 * сущность не отображается.
		 */
		User deletedUser = null;
		try {
			deletedUser = userService.get(id);
		} catch (Exception e) {
			logger.error("Can\'t get user with id: ", id);
			String error = "Ошибка при запросе пользователя c id: " + id + " из базы данных";
			request.getSession().setAttribute("error", error);
		}
		if (deletedUser != null) {
			deletedUser.setDeleted(true);
			try {
				userService.save(deletedUser);
			} catch (Exception e) {
				logger.error("Can\'t delete user with id: ", id);
				String error = "Ошибка при удалении пользователя c id: " + id + " из базы данных";
				request.getSession().setAttribute("error", error);
			}
			String success = "Пользователь с id:" + id + " и логином: " + deletedUser.getName()
				+ " успешно удалён";
			request.getSession().setAttribute("success", success);
		}
		return "redirect:/director/stuff";
	}

	//---------------------- Customer block ---------------------
	@RequestMapping(value = {"/director/customers"}, method = RequestMethod.GET)
	public ModelAndView listCustomers(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/directorView/DirectorCustomersBoard");
		injectMessageToPage(request, model);
		try {
			model.addObject("allCustomers", customerService.getAll());
		} catch (Exception e) {
			logger.error("Can\'t get customer list", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/customers/delete/{id}"}, method = RequestMethod.GET)
	public String deleteCustomer(@PathVariable("id") Long id, HttpServletRequest request) {
		logger.info("Deleting user with id: ", id);

		/*
		 * Перед удалением получаем покупателя из базы данных и устанавливаем значение поля
		 * Deleted в true. По умолчание у всех установлено значение false. Далее данная
		 * сущность не отображается.
		 */
		Customer deletedCustomer = null;
		try {
			deletedCustomer = customerService.get(id);
		} catch (Exception e) {
			logger.error("Can\'t get customer with id: ", id);
			String error = "Ошибка при запросе пользователя c id: " + id + " из базы данных";
			request.getSession().setAttribute("error", error);
		}
		if (deletedCustomer != null) {
			deletedCustomer.setDeleted(true);
			try {
				customerService.save(deletedCustomer);
				String success =
					"Покупатель с id:" + id + " и именем: " + deletedCustomer.getFirstName() + " "
						+ deletedCustomer.getSecName() + " успешно удалён";
				request.getSession().setAttribute("success", success);
			} catch (Exception e) {
				logger.error("Can\'t delete customer with id: ", id);
				String error = "Ошибка при удалении покупателя c id: " + id + " из базы данных";
				request.getSession().setAttribute("error", error);
			}
		}
		return "redirect:/director/customers";
	}

	@RequestMapping(value = {"/director/customer"}, method = RequestMethod.GET)
	public ModelAndView panelCustomer(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/directorView/DirectorCustomer");

		/*
		 * Вначале извлекаем информацию о успешности выполнения предыдущих операций,
		 * если таковые имели место. Если есть, то передаём их в model чтобы отобразить на странице.
		 */
		injectMessageToPage(request, model);
		model.addObject("customer", new Customer());
		model.addObject("delivery", new Delivery());
		return model;
	}

	@RequestMapping(value = {"/director/customer/create"}, method = RequestMethod.POST)
	public String createCustomer(@ModelAttribute("customer") Customer incomingCustomer,
		@ModelAttribute("delivery") Delivery incomingDelivery, HttpServletRequest request) {

		/*
		 * Ищем в базе покупателя с такой же почтой или телефоном.
		 * В случае нахождения покупателя с такой же почтой или телефоном генерим сообщение error.
		 * Если же ничего не находим то записываем покупателя в базу данных и создаём сообщение о усрехе.
		 */
		String searchingEmail = incomingCustomer.getEmail();
		String searchingPhone = incomingCustomer.getPhone();
		Customer foundCustomerByEmail = customerService.getByEmail(searchingEmail);
		Customer foundCustomerByPhone = customerService.getByPhone(searchingPhone);
		if (foundCustomerByEmail != null) { //проверяем есть ли покупатель с такой почтой
			String error = "Покупатель с такой почтой: " + searchingEmail + " уже существует";
			request.getSession().setAttribute("error", error);
		} else if (foundCustomerByPhone != null) { //проверяем есть ли покупатель с таким телефоном
			String error = "Покуптаель с таким телефоном" + searchingPhone + " уже существует";
			request.getSession().setAttribute("error", error);
		} else if ((incomingCustomer.getPhone() == null) || ""
			.equals(incomingCustomer.getPhone())) { //проверка что поле телефона не пустое
			String error = "Необходимо указать телепхон";
			request.getSession().setAttribute("error", error);
		} else if ((incomingCustomer.getEmail() == null) || ""
			.equals(incomingCustomer.getEmail())) { //проверка что поле почты не пустое
			String error = "Необходимо указать Email";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				incomingCustomer.setCreationDate(new Date());
				List<Delivery> deliveryList = new ArrayList<>();
				deliveryList.add(incomingDelivery);
				incomingCustomer.setDeliveries(deliveryList);
				deliveryService.save(incomingDelivery);
				customerService.save(incomingCustomer);
				String success = "Клиент успешно создан";
				request.getSession().setAttribute("success", success);
			} catch (Exception e) {
				logger.error("Can\'t save customer", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}
		}
		return "redirect:/director/customer";
	}

	@RequestMapping(value = {"/director/customer/edit/{id}"}, method = RequestMethod.GET)
	public ModelAndView editCustomer(@PathVariable("id") Long id, HttpServletRequest request) {
		logger.info("Edit customer with id: ", id);
		ModelAndView model = new ModelAndView();
		injectMessageToPage(request, model);
		Customer customer = null;
		try {
			customer = customerService.get(id);
		} catch (Exception e) {
			logger.error("Can\'t get customer with id: ", id);
			String error = "Такой клиент не существует";
			request.getSession().setAttribute("error", error);
			model.setViewName("redirect:/director/customers");
			return model;
		}
		if (customer != null) {
			model.setViewName("/directorView/DirectorCustomerEdit");
			model.addObject("customer", customer);
			model.addObject("newdelivery", new Delivery());
		} else {
			String error = "Клиент не найден";
			request.getSession().setAttribute("error", error);
			model.setViewName("redirect:/director/customers");
		}
		return model;
	}

	@RequestMapping(value = {"/director/customer/adddelivery/{id}"}, method = RequestMethod.POST)
	public ModelAndView addDelivery(@PathVariable("id") Long id,
		@ModelAttribute("newdelivery") Delivery incomingDelivery, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		Customer customer = null;
		try {
			customer = customerService.get(id);
		} catch (Exception e) {
			logger.error("Can\'t get customer with id: ", id);
			String error = "При добавлении адреса произошла ошибка обращения к базе данных";
			request.getSession().setAttribute("error", error);
			model.setViewName("redirect:/director/customers");
			return model;
		}
		try {
			customer.getDeliveries().add(incomingDelivery);
			deliveryService.save(incomingDelivery);
			customerService.save(customer);
			String success = "Адрес доставки успешно добавлен.";
			request.getSession().setAttribute("success", success);
			model.setViewName("redirect:/director/customer/edit/" + customer.getId());
		} catch (Exception e) {
			logger.error("Can\'t save delivery", e);
			String error = "Ошибка при записи в базу данных";
			request.getSession().setAttribute("error", error);
		}
		return model;
	}

	@RequestMapping(value = {"/director/customer/update"}, method = RequestMethod.POST)
	public String updateCustomer(@ModelAttribute("customer") Customer incomingCustomer,
		@ModelAttribute("includeDeliveries") HttpServletRequest request) {

		/*
		 * Ищем в базе пользователя с таким же логином.
		 * В случае нахождения пользователя с таким логином, проверяем id и в случае если они разные генерим сообщение
		 * error.
		 * Если же ничего не находим или если id совпадают, то обновляем пользователя в базе данных и создаём
		 * сообщение о усрехе.
		 */
		Customer foundCustomer = customerService.getByEmail(incomingCustomer.getEmail());
		if ((foundCustomer != null) && !(foundCustomer.getId().equals(incomingCustomer.getId()))) {
			String error =
				"Покупатель с такой почтой: " + incomingCustomer.getEmail() + " уже существует";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				customerService.save(incomingCustomer);
				String success = "Информация о покупателе успешно обновлена в базе данных.";
				request.getSession().setAttribute("success", success);
			} catch (Exception e) {
				logger.error("Can\'t update customer", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}
		}
		return "redirect:/director/customer/edit/" + incomingCustomer.getId();
	}

	@RequestMapping(value = {
		"/director/customer/removedelivery/{customerId}/{deliveryIndex}"}, method = RequestMethod.GET)
	public ModelAndView removeDelivery(@PathVariable("customerId") Long customerId,
		@PathVariable("deliveryIndex") int deliveryIndex, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		Customer customer = null;
		try {
			customer = customerService.get(customerId);
		} catch (Exception e) {
			logger.error("Can\'t get customer with id: ", customerId);
			String error = "При поппытке удаления адреса произошла ошибка обращения к базе данных.";
			request.getSession().setAttribute("error", error);
			model.setViewName("redirect:/director/customers");
			return model;
		}
		try {
			customer.getDeliveries().remove(deliveryIndex);
			customerService.save(customer);
			String success = "Адрес доставки удален.";
			request.getSession().setAttribute("success", success);
			model.setViewName("redirect:/director/customer/edit/" + customer.getId());
		} catch (Exception e) {
			logger.error("Can\'t save delivery", e);
			String error = "Ошибка при записи в базу данных";
			request.getSession().setAttribute("error", error);
		}
		return model;
	}

	@RequestMapping(value = {"/director/statistic/middle/"}, method = RequestMethod.GET)
	public ModelAndView statisticAverage() {
		return new ModelAndView("/directorView/DirectorStatisticAverage");
	}

	@RequestMapping(value = {"/director/statistic/sum/"}, method = RequestMethod.GET)
	public ModelAndView statisticSum() {
		return new ModelAndView("/directorView/DirectorStatisticSum");
	}

	@RequestMapping(value = {"/director/statistic/geo/"}, method = RequestMethod.GET)
	public ModelAndView statisticGeo() {
		return new ModelAndView("/directorView/DirectorStatisticGeo");
	}

	@RequestMapping(value = {"/director/statistic/newCustomers/"}, method = RequestMethod.GET)
	public ModelAndView statisticNewCustomers() {
		return new ModelAndView("/directorView/DirectorStatisticNewCustomer");
	}

	//Меняем менеджер заказа
	@RequestMapping(value = {
		"/director/order/change/{id}/manager/{managerId}"}, method = RequestMethod.GET)
	public ModelAndView changeManager(@PathVariable("id") Long id,
		@PathVariable("managerId") Long managerId) {
		try {
			Order order = orderService.get(id);
			User manager = userService.get(managerId);
			order.setManager(manager);
			orderService.save(order);
		} catch (Exception e) {
			logger.error("while changing manager in order, order's id={}", id);
		}
		return new ModelAndView("redirect:/manager/order/update/" + id);
	}

	//---------------------- Product block ---------------------

	@RequestMapping(value = {"/director/controlpanel/product"}, method = RequestMethod.GET)
	public ModelAndView controlPanelProduct(HttpServletRequest request) {

		ModelAndView model = new ModelAndView("/directorView/ControlPanelProduct");

		injectMessageToPage(request, model);

		try {
			model.addObject("productes", productService.getAll());
			model.addObject("product", new Product());
		} catch (Exception e) {
			logger.error("Can\'t get product list {}", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/production/save"}, method = RequestMethod.POST)
	public ModelAndView saveProduct(@ModelAttribute("product") @Valid Product incomingProduct,
		BindingResult bindingResult, HttpServletRequest request) {

		ModelAndView model = new ModelAndView();

		/*
		 * Ищем в базе продукт с таким же именем.
		 * В случае нахождения продукта с таким именем генерим сообщение error.
		 * Если же ничего не находим то записываем продукт в базу данных и создаём сообщение о усрехе.
		 */

		if (bindingResult.hasErrors()) {
			model.setViewName("/directorView/ControlPanelProduct");
			model.addObject("productes", productService.getAll());
			return model;
		} else {
			String searchingProduct = incomingProduct.getProductName();
			Product foundProduct = productService.getByName(searchingProduct);
			if ((foundProduct != null) &&
				!(foundProduct.getId().equals(incomingProduct.getId()))) { //проверяем есть ли продукция с таким названием
				String error = "Продукт с названием: " + searchingProduct + " уже существует";
				request.getSession().setAttribute("error", error);
			} else {
				try {
					productService.save(incomingProduct);
					String success = "Продукт с названием: " + searchingProduct + " успешно создан";
					request.getSession().setAttribute("success", success);
				} catch (Exception e) {
					logger.error("Can\'t save product {}", e);
					String error = "При сохранении продукта произошла ошибка";
					request.getSession().setAttribute("error", error);
				}
			}
		}
		model.setViewName("redirect:/director/controlpanel/product");
		return model;

	}

	@RequestMapping(value = {"/director/controlpanel/product/delete/{id}"}, method = RequestMethod.GET)
	public String deleteProduct(@PathVariable("id") Long id, HttpServletRequest request) {

		logger.info("Deleting product with id: {}", id);

		/*
		 * Перед удалением получаем продукт из базы данных.
		 */
		Product deletedProduct = null;
		try {
			deletedProduct = productService.get(id);
		} catch (Exception e) {
			logger.error("Can\'t get product with id: ", id);
			String error = "При удалении продукта произошла ошибка обращения к базе данных";
			request.getSession().setAttribute("error", error);
		}
		if (deletedProduct != null) {
			try {
				productService.delete(deletedProduct);
				String success = "Продукт " + deletedProduct.getProductName() + " успешно удалён";
				request.getSession().setAttribute("success", success);
			} catch (Exception e) {
				logger.error("Can\'t delete product with id: {}", id);
				String error = "Ошибка при удалении продукта из базы данных";
				request.getSession().setAttribute("error", error);
			}
		}
		return "redirect:/director/controlpanel/product";
	}

	@RequestMapping(value = {"/director/controlpanel/product/edit/{id}"}, method = RequestMethod.GET)
	public String editProduct(@PathVariable("id") Long id, Model model,
		HttpServletRequest request) {

		logger.info("Edit product with id: {}", id);

		Product product = null;
		try {
			product = productService.get(id);
		} catch (Exception e) {
			logger.error("Can\'t get product with id: {}", id);
			String error = "При редактировании продукта произошла ошибка обращения к базе данных";
			request.getSession().setAttribute("error", error);
			return "redirect:/director/controlpanel/product";
		}

		if (product != null) {
			model.addAttribute("product", product);
			model.addAttribute("productes", productService.getAll());
			return "directorView/ControlPanelProduct";
		} else {
			String error = "Такой продукт не найден";
			request.getSession().setAttribute("error", error);
			return "redirect:/director/controlpanel/product";
		}
	}

	//---------------------- Model of Phone block ---------------------

	@RequestMapping(value = {"/director/controlpanel/model"}, method = RequestMethod.GET)
	public ModelAndView controlPanelModelPhone(HttpServletRequest request) {

		ModelAndView model = new ModelAndView("/directorView/ControlPanelModelPhone");

		injectMessageToPage(request, model);

		try {
			model.addObject("allModels", phoneModelService.getAll());
			model.addObject("phoneModel", new PhoneModel());
		} catch (Exception e) {
			logger.error("Can\'t get model of phone list {}", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/model/save"}, method = RequestMethod.POST)
	public ModelAndView savePhoneModel(@ModelAttribute("phoneModel") @Valid PhoneModel incomingPhoneModel,
		BindingResult bindingResult, HttpServletRequest request) {

		ModelAndView model = new ModelAndView();

		/*
		 * Ищем в базе модель с таким же именем.
		 * В случае нахождения модели с таким именем генерим сообщение error.
		 * Если же ничего не находим то записываем модель в базу данных и создаём сообщение о усрехе.
		 */

		if (bindingResult.hasErrors()) {
			model.setViewName("/directorView/ControlPanelModelPhone");
			model.addObject("allModels", phoneModelService.getAll());
			return model;
		} else {
			String searchingModel = incomingPhoneModel.getModelName();
			PhoneModel foundModel = phoneModelService.getByName(searchingModel);
			if ((foundModel != null) &&
				!(foundModel.getId().equals(incomingPhoneModel.getId()))) { //проверяем есть ли модель с таким названием
				String error = "Модель с названием: " + searchingModel + " уже существует";
				request.getSession().setAttribute("error", error);
			} else {
				try {
					phoneModelService.save(incomingPhoneModel);
					String success = "Модель с названием: " + searchingModel + " успешно создана";
					request.getSession().setAttribute("success", success);
				} catch (Exception e) {
					logger.error("Can\'t save model of phone {}", e);
					String error = "При сохранении модели телефона произошла ошибка";
					request.getSession().setAttribute("error", error);
				}
			}
		}
		model.setViewName("redirect:/director/controlpanel/model");
		return model;

	}

	@RequestMapping(value = {"/director/controlpanel/model/edit/{id}"}, method = RequestMethod.GET)
	public String editPhoneModel(@PathVariable("id") Long id, Model model,
		HttpServletRequest request) {

		logger.info("Edit model of phone with id: {}", id);

		PhoneModel phoneModel = null;
		try {
			phoneModel = phoneModelService.get(id);
		} catch (Exception e) {
			logger.error("Can\'t get model of phone with id: {}", id);
			String error = "При редактировании модели произошла ошибка обращения к базе данных";
			request.getSession().setAttribute("error", error);
			return "redirect:/director/controlpanel/model";
		}

		if (phoneModel != null) {
			model.addAttribute("phoneModel", phoneModel);
			model.addAttribute("allModels", phoneModelService.getAll());
			return "directorView/ControlPanelModelPhone";
		} else {
			String error = "Такая модель не найдена";
			request.getSession().setAttribute("error", error);
			return "redirect:/director/controlpanel/model";
		}
	}

	@RequestMapping(value = {"/director/controlpanel/model/delete/{id}"}, method = RequestMethod.GET)
	public String deletePhoneModel(@PathVariable("id") Long id, HttpServletRequest request) {

		logger.info("Deleting model of phone with id: {}", id);

		/*
		 * Перед удалением получаем модель из базы данных.
		 */
		PhoneModel deletedPhoneModel = null;
		try {
			deletedPhoneModel = phoneModelService.get(id);
		} catch (Exception e) {
			logger.error("Can\'t get model with id: {}", id);
			String error = "При удалении модели произошла ошибка обращения к базе данных";
			request.getSession().setAttribute("error", error);
		}
		if (deletedPhoneModel != null) {
			try {
				phoneModelService.delete(deletedPhoneModel);
				String success = "Модель " + deletedPhoneModel.getModelName() + " успешно удалёна";
				request.getSession().setAttribute("success", success);
			} catch (Exception e) {
				logger.error("Can\'t delete model of phone with id: ", id);
				String error = "Ошибка при удалении модели телефона из базы данных";
				request.getSession().setAttribute("error", error);
			}
		}
		return "redirect:/director/controlpanel/model";
	}


	private void injectMessageToPage(HttpServletRequest request, ModelAndView model) {
		String success = (String) request.getSession().getAttribute("success");
		String error = (String) request.getSession().getAttribute("error");
		if (success != null) {
			model.addObject("success", success);
			request.getSession().removeAttribute("success");
		} else if (error != null) {
			model.addObject("error", error);
			request.getSession().removeAttribute("error");
		}
	}

	//Выборка тех заказов где есть уведомления для конкретного пользователя
	@RequestMapping(value = {"/order/director/notification/get"}, method = RequestMethod.GET)
	public ModelAndView getOrdersByNotification() {
		ModelAndView model = new ModelAndView("/directorView/DirectorDashBoard");
		String user = userService.getCurrentUser().getName();
		List<Notification> myNotes = notificationService.findAllByUser(user);
		List<Order> masterOrders = new ArrayList<>();
		for (Notification n : myNotes) {
			masterOrders.add(orderService.get(n.getOrder()));
		}
		model.addObject("orders", masterOrders);
		return model;
	}


}
