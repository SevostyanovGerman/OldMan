package main.controller;

import main.model.Role;
import main.model.Status;
import main.model.User;
import main.service.OrderService;
import main.service.RoleService;
import main.service.StatusService;
import main.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DirectorController {

	private UserService userService;

	private OrderService orderService;

	private StatusService statusService;

	private RoleService roleService;

	private final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	public DirectorController (UserService userService, OrderService orderService,
							   StatusService statusService, RoleService roleService){
		this.userService = userService;
		this.orderService = orderService;
		this.statusService = statusService;
		this.roleService = roleService;
	}

	@RequestMapping(value = {"/director"}, method = RequestMethod.GET)
	public ModelAndView director () {
		ModelAndView model = new ModelAndView("/directorView/DirectorDashBoard");

		try {
			model.addObject("orders", orderService.getAll());
		} catch (Exception e) {
			logger.error("Can\'t getById all orders", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/stuff"}, method = RequestMethod.GET)
	public ModelAndView showStaff (HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/directorView/DirectorStuffBoard");

		String success = (String) request.getSession().getAttribute("success");
		String error = (String) request.getSession().getAttribute("error");

		if(success != null){
			model.addObject("success", success);
			request.getSession().removeAttribute("success");
		} else if(error != null){
			model.addObject("error", error);
			request.getSession().removeAttribute("error");
		}

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
	public ModelAndView controlPanel () {
		ModelAndView model = new ModelAndView("/directorView/ControlPanel");

		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/statuses"}, method = RequestMethod.GET)
	public ModelAndView controlPanelStatus (HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/directorView/ControlPanelStatus");

		/*
		 * Вначале извлекаем информацию о успешности выполнения предыдущих операциях,
		 * если таковые имели место. Если есть, то передаём их в model чтобы отобразить на странице.
		 */

		String success = (String) request.getSession().getAttribute("success");
		String error = (String) request.getSession().getAttribute("error");

		if(success != null){
			model.addObject("success", success);
			request.getSession().removeAttribute("success");
		} else if(error != null){
			model.addObject("error", error);
			request.getSession().removeAttribute("error");
		}

		try {
			model.addObject("statuses", statusService.getAll());
			model.addObject("newstatus", new Status());
		} catch (Exception e) {
			logger.error("Can\'t getById status list", e);
		}

		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/statuses/create"}, method = RequestMethod.POST)
	public String createStatus (@ModelAttribute("status") Status status, HttpServletRequest request) {

		/*
		 * Ищем в базе статус с таким же именем.
		 * В случае нахождения статуса с таким именем генерим сообщение error.
		 * Если же ничего не находим то записываем статус в базу данных и создаём сообщение о усрехе.
		 */
		String searchingStatus = status.getName();
		long searchingIndex = status.getNumber();

		Status foundStatus = statusService.getByName(searchingStatus);
		Status foundStatusByNumber = statusService.get(searchingIndex);

		if(foundStatus != null){
			String error = "Статус с именем: " + searchingStatus + " уже существует";
			request.getSession().setAttribute("error", error);
		} else if( (foundStatusByNumber != null) && (searchingIndex != 0)){
			String error = "Статус с индексом: " + searchingIndex + " уже существует";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				statusService.save(status);
			} catch (Exception e) {
				logger.error("Can\'t create status", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Статус с именем: " + searchingStatus + " успешно создан";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/statuses";
	}

	@RequestMapping(value = {"/director/controlpanel/statuses/update"}, method = RequestMethod.POST)
	public String updateStatus (@ModelAttribute("status") Status incomingStatus, HttpServletRequest request) {

		/*
		 * Ищем в базе статус с таким же именем.
		 * В случае нахождения статуса с таким именем, проверяем id и в случае если они разные генерим сообщение error.
		 * Если же ничего не находим или если id совпадают, то обновляем статус в базе данных и создаём сообщение о усрехе.
		 */
		long number = incomingStatus.getNumber();
		Status foundStatus = statusService.getByName(incomingStatus.getName());
		Status foundStatusByNumber = statusService.get(number);

		if((foundStatus != null) && (incomingStatus.getId() != foundStatus.getId())){
			String error = "Статус с именем: " + incomingStatus.getName() + " уже существует";
			request.getSession().setAttribute("error", error);
		} else if((foundStatusByNumber != null) && (number > 0) && (incomingStatus.getId() != foundStatus.getId())){
			String error = "Статус с индексом: " + incomingStatus.getName() + " уже существует. Допустимо дублирование только индекс: 0";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				statusService.save(incomingStatus);
			} catch (Exception e) {
				logger.error("Can\'t save status", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Статус успешно изменён.";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/statuses";
	}

	@RequestMapping(value = {"/director/controlpanel/status/delete/{id}"}, method = RequestMethod.GET)
	public String deleteStatus (@PathVariable("id") Long id, HttpServletRequest request) {

		logger.info("Deleting status with id: ", id);

		/*
		 * Перед удалением получаем статус из базы данных и устанавливаем значение поля
		 * Deleted в true. По умолчание у всех установлено значение false. Далее данная
		 * сущность не отображается.
		 */

		Status deletedStatus = null;
		try {
			deletedStatus = statusService.getById(id);
		} catch (Exception e){
			logger.error("Can\'t getById status with id: ", id);
			String error = "Ошибка при запросе статуса c id: " + id + " из базы данных";
			request.getSession().setAttribute("error", error);
		}

		if (deletedStatus != null){
			deletedStatus.setDeleted(true);
			try {
				statusService.save(deletedStatus);
			} catch (Exception e){
				logger.error("Can\'t delete status with id: ", id);
				String error = "Ошибка при удалении статуса c id: " + id + " из базы данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Статус с id:" + id + " и именем: " + deletedStatus.getName() + " успешно удалён";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/statuses";
	}


	//---------------------- Role block ---------------------

	@RequestMapping(value = {"/director/controlpanel/roles"}, method = RequestMethod.GET)
	public ModelAndView controlPanelRole (HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/directorView/ControlPanelRole");

		/*
		 * Вначале извлекаем информацию о успешности выполнения предыдущих операциях,
		 * если таковые имели место. Если есть, то передаём их в model чтобы отобразить на странице.
		 */

		String success = (String) request.getSession().getAttribute("success");
		String error = (String) request.getSession().getAttribute("error");

		if(success != null){
			model.addObject("success", success);
			request.getSession().removeAttribute("success");
		} else if(error != null){
			model.addObject("error", error);
			request.getSession().removeAttribute("error");
		}

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
	public String createRole (@ModelAttribute("role") Role incomingRole, HttpServletRequest request) {

		/*
		 * Ищем в базе должность с таким же именем.
		 * В случае нахождения должности с таким именем генерим сообщение error.
		 * Если же ничего не находим то записываем должность в базу данных и создаём сообщение о усрехе.
		 */
		String searchingRole = incomingRole.getName();
		Role foundRole = roleService.getByName(searchingRole);

		if(foundRole != null){
			String error = "Должность с именем: " + searchingRole + " уже существует";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				roleService.save(incomingRole);
			} catch (Exception e) {
				logger.error("Can\'t save role", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Должность с именем: " + searchingRole + " успешно создана";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/roles";
	}

	@RequestMapping(value = {"/director/controlpanel/roles/update"}, method = RequestMethod.POST)
	public String updateRole (@ModelAttribute("role") Role incomingRole, HttpServletRequest request) {

		/*
		 * Ищем в базе должность с таким же именем.
		 * В случае нахождения должности с таким именем, проверяем id и в случае если они разные генерим сообщение error.
		 * Если же ничего не находим или если id совпадают, то обновляем должость в базе данных и создаём сообщение о усрехе.
		 */
		Role foundRole = roleService.getByName(incomingRole.getName());

		if((foundRole != null) && (incomingRole.getId() != foundRole.getId())){
			String error = "Должность с именем: " + incomingRole.getName() + " уже существует";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				roleService.save(incomingRole);
			} catch (Exception e) {
				logger.error("Can\'t save role", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Должность  успешно изменёна.";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/roles";
	}

	@RequestMapping(value = {"/director/controlpanel/role/edit/{id}"}, method = RequestMethod.GET)
	public ModelAndView editRole (@PathVariable("id") Long id, HttpServletRequest request) {

		ModelAndView model = new ModelAndView();

		/*
		 * Ищем в базе должность с таким id.
		 * В случае нахождения роли с таким id, добавляем его в model.
		 * Если же ничего не находим, то создаём сообщение о ошибке.
		 */
		Role role = roleService.get(id);

		if(role == null){
			String error = "Должность с id: " + id + " не найдена";
			request.getSession().setAttribute("error", error);
			model.setViewName("redirect:/director/controlpanel/roles");
		} else {
			model.setViewName("/directorView/ControlPanelRole");
			model.addObject("roles", roleService.getAll());
			model.addObject("allStatuses", statusService.getAll());
			model.addObject("role", role);
		}

		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/role/delete/{id}"}, method = RequestMethod.GET)
	public String deleteRole (@PathVariable("id") Long id, HttpServletRequest request) {

		logger.info("Deleting role with id: ", id);

		/*
		 * Перед удалением получаем должность из базы данных и устанавливаем значение поля
		 * Deleted в true. По умолчание у всех установлено значение false. Далее данная
		 * сущность не отображается.
		 */

		Role deletedRole = null;
		try {
			deletedRole = roleService.get(id);
		} catch (Exception e){
			logger.error("Can\'t getById role with id: ", id);
			String error = "Ошибка при запросе должности c id: " + id + " из базы данных";
			request.getSession().setAttribute("error", error);
		}

		if (deletedRole != null){
			deletedRole.setDeleted(true);
			try {
				roleService.save(deletedRole);
			} catch (Exception e){
				logger.error("Can\'t delete role with id: ", id);
				String error = "Ошибка при удалении должности c id: " + id + " из базы данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Должность с id:" + id + " и именем: " + deletedRole.getName() + " успешно удалёна";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/roles";
	}


	//---------------------- User block ---------------------

	@RequestMapping(value = {"/director/controlpanel/user"}, method = RequestMethod.GET)
	public ModelAndView controlPanelUser (HttpServletRequest request) {
		ModelAndView model = new ModelAndView("/directorView/ControlPanelUser");

		/*
		 * Вначале извлекаем информацию о успешности выполнения предыдущих операциях,
		 * если таковые имели место. Если есть, то передаём их в model чтобы отобразить на странице.
		 */

		String success = (String) request.getSession().getAttribute("success");
		String error = (String) request.getSession().getAttribute("error");

		if(success != null){
			model.addObject("success", success);
			request.getSession().removeAttribute("success");
		} else if(error != null){
			model.addObject("error", error);
			request.getSession().removeAttribute("error");
		}

		try {
			model.addObject("allRoles", roleService.getAll());
			model.addObject("user", new User());
		} catch (Exception e) {
			logger.error("Can\'t get user list", e);
		}

		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/user/create"}, method = RequestMethod.POST)
	public String createUser (@ModelAttribute("user") User incomingUser, HttpServletRequest request) {

		/*
		 * Ищем в базе роль с таким же именем.
		 * В случае нахождения роли с таким именем генерим сообщение error.
		 * Если же ничего не находим то записываем роль в базу данных и создаём сообщение о усрехе.
		 */
		String searchingUser = incomingUser.getName();
		String searchingEmail = incomingUser.getEmail();
		User foundUser = userService.getByName(searchingUser);
		User foundUserByEmail = userService.getByEmail(searchingEmail);

		if(foundUser != null){ //проверяем есть ли сотрудник с таким логином
			String error = "Пользователь с логином: " + searchingUser + " уже существует";
			request.getSession().setAttribute("error", error);
		} else if((incomingUser.getPassword() == null) || "".equals(incomingUser.getPassword())){ //проверка что поле пароля не пустое
			String error = "Пароль не может быть пустым";
			request.getSession().setAttribute("error", error);
		} else if(foundUserByEmail != null){ //проверяем есть ли сотрудник с такой почтой
			String error = "Сотрудник с таким email уже существует";
			request.getSession().setAttribute("error", error);
		} else if((incomingUser.getPhone() == null) || "".equals(incomingUser.getPhone())){ ////проверка что поле телефона не пустое
			String error = "Необходимо указать телепхон";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				incomingUser.setCreated(new Date());
				userService.save(incomingUser);
			} catch (Exception e) {
				logger.error("Can\'t save user", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Пользователь с логином: " + searchingUser + " успешно создан";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/user";
	}

	@RequestMapping(value = {"/director/controlpanel/user/update"}, method = RequestMethod.POST)
	public String updateUser (@ModelAttribute("user") User incomingUser, HttpServletRequest request) {

		/*
		 * Ищем в базе пользователя с таким же логином.
		 * В случае нахождения пользователя с таким логином, проверяем id и в случае если они разные генерим сообщение error.
		 * Если же ничего не находим или если id совпадают, то обновляем пользователя в базе данных и создаём сообщение о усрехе.
		 */
		User foundUser = userService.getByName(incomingUser.getName());

		if((foundUser != null) && (incomingUser.getId() != foundUser.getId())){
			String error = "Пользователь с логином: " + incomingUser.getName() + " уже существует";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				userService.save(incomingUser);
			} catch (Exception e) {
				logger.error("Can\'t update user", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Информация о пользователе успешно записана в базе данных.";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/user";
	}

	@RequestMapping(value = {"/director/controlpanel/user/edit/{id}"}, method = RequestMethod.GET)
	public ModelAndView editUser (@PathVariable("id") Long id, HttpServletRequest request) {

		logger.info("Edit user with id: ", id);

		ModelAndView model = new ModelAndView();

		User user = null;
		try {
			user = userService.get(id);
		} catch (Exception e){
			logger.error("Can\'t get user with id: ", id);
			String error = "Ошибка при обращении к базе данных";
			request.getSession().setAttribute("error", error);

			model.setViewName("redirect:/director/stuff");

			return model;
		}

		if (user != null){
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
	public String deleteUser (@PathVariable("id") Long id, HttpServletRequest request) {

		logger.info("Deleting user with id: ", id);

		/*
		 * Перед удалением получаем пользователя из базы данных и устанавливаем значение поля
		 * Deleted в true. По умолчание у всех установлено значение false. Далее данная
		 * сущность не отображается.
		 */

		User deletedUser = null;
		try {
			deletedUser = userService.get(id);
		} catch (Exception e){
			logger.error("Can\'t getById user with id: ", id);
			String error = "Ошибка при запросе пользователя c id: " + id + " из базы данных";
			request.getSession().setAttribute("error", error);
		}

		if (deletedUser != null){
			deletedUser.setDeleted(true);
			try {
				userService.save(deletedUser);
			} catch (Exception e){
				logger.error("Can\'t delete user with id: ", id);
				String error = "Ошибка при удалении пользователя c id: " + id + " из базы данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Пользователь с id:" + id + " и логином: " + deletedUser.getName() + " успешно удалён";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/stuff";
	}
}
