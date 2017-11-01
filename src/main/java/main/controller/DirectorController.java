package main.controller;

import main.model.Role;
import main.model.Status;
import main.service.OrderService;
import main.service.RoleService;
import main.service.StatusService;
import main.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
			logger.error("Can\'t get all orders", e);
		}
		return model;
	}

	@RequestMapping(value = {"/director/stuff"}, method = RequestMethod.GET)
	public ModelAndView showStaff () {
		ModelAndView model = new ModelAndView("/directorView/DirectorStuffBoard");
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
			logger.error("Can\'t get status list", e);
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
		Status foundStatus = statusService.getByName(searchingStatus);

		if(foundStatus != null){
			String error = "Статус с именем: " + searchingStatus + " уже существует";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				statusService.save(status);
			} catch (Exception e) {
				logger.error("Can\'t save status", e);
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
		Status foundStatus = statusService.getByName(incomingStatus.getName());

		if((foundStatus != null) && (incomingStatus.getId() != foundStatus.getId())){
			String error = "Статус с именем: " + incomingStatus.getName() + " уже существует";
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
			deletedStatus = statusService.get(id);
		} catch (Exception e){
			logger.error("Can\'t get status with id: ", id);
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
			model.addObject("newrole", new Role());
		} catch (Exception e) {
			logger.error("Can\'t get role list", e);
		}

		return model;
	}

	@RequestMapping(value = {"/director/controlpanel/roles/create"}, method = RequestMethod.POST)
	public String createStatus (@ModelAttribute("role") Role incomingRole, HttpServletRequest request) {

		/*
		 * Ищем в базе роль с таким же именем.
		 * В случае нахождения роли с таким именем генерим сообщение error.
		 * Если же ничего не находим то записываем роль в базу данных и создаём сообщение о усрехе.
		 */
		String searchingRole = incomingRole.getName();
		Role foundRole = roleService.getByName(searchingRole);

		if(foundRole != null){
			String error = "Статус с именем: " + searchingRole + " уже существует";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				roleService.save(incomingRole);
			} catch (Exception e) {
				logger.error("Can\'t save status", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Статус с именем: " + searchingRole + " успешно создан";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/roles";
	}

	@RequestMapping(value = {"/director/controlpanel/roles/update"}, method = RequestMethod.POST)
	public String updateRole (@ModelAttribute("status") Role incomingRole, HttpServletRequest request) {

		/*
		 * Ищем в базе роль с таким же именем.
		 * В случае нахождения роли с таким именем, проверяем id и в случае если они разные генерим сообщение error.
		 * Если же ничего не находим или если id совпадают, то обновляем статус в базе данных и создаём сообщение о усрехе.
		 */
		Role foundRole = roleService.getByName(incomingRole.getName());

		if((foundRole != null) && (incomingRole.getId() != foundRole.getId())){
			String error = "Роль с именем: " + incomingRole.getName() + " уже существует";
			request.getSession().setAttribute("error", error);
		} else {
			try {
				roleService.save(incomingRole);
			} catch (Exception e) {
				logger.error("Can\'t save status", e);
				String error = "Ошибка при записи в базу данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Роль успешно изменёна.";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/roles";
	}

	@RequestMapping(value = {"/director/controlpanel/role/delete/{id}"}, method = RequestMethod.GET)
	public String deleteRole (@PathVariable("id") Long id, HttpServletRequest request) {

		logger.info("Deleting role with id: ", id);

		/*
		 * Перед удалением получаем роль из базы данных и устанавливаем значение поля
		 * Deleted в true. По умолчание у всех установлено значение false. Далее данная
		 * сущность не отображается.
		 */

		Role deletedRole = null;
		try {
			deletedRole = roleService.get(id);
		} catch (Exception e){
			logger.error("Can\'t get role with id: ", id);
			String error = "Ошибка при запросе роли c id: " + id + " из базы данных";
			request.getSession().setAttribute("error", error);
		}

		if (deletedRole != null){
			deletedRole.setDeleted(true);
			try {
				roleService.save(deletedRole);
			} catch (Exception e){
				logger.error("Can\'t delete role with id: ", id);
				String error = "Ошибка при удалении роли c id: " + id + " из базы данных";
				request.getSession().setAttribute("error", error);
			}

			String success = "Роль с id:" + id + " и именем: " + deletedRole.getName() + " успешно удалёна";
			request.getSession().setAttribute("success", success);
		}

		return "redirect:/director/controlpanel/roles";
	}

}
