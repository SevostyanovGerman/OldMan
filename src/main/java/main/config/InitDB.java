package main.config;

import java.text.ParseException;
import java.util.HashSet;
import javax.annotation.PostConstruct;
import main.model.DeliveryType;
import main.model.FuncMenu;
import main.model.Payment;
import main.model.Role;
import main.model.Status;
import main.model.User;
import main.service.CustomerService;
import main.service.DeliveryService;
import main.service.DeliveryTypeService;
import main.service.FuncMenuService;
import main.service.ItemService;
import main.service.MailService;
import main.service.OrderService;
import main.service.PaymentService;
import main.service.PhoneModelService;
import main.service.ProductService;
import main.service.RoleService;
import main.service.StatusService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitDB {

	private UserService userService;

	private RoleService roleService;

	private PaymentService paymentService;

	private StatusService statusService;

	private DeliveryTypeService deliveryTypeService;

	private FuncMenuService functionService;


	private final static String ColorNewStatus = "#C5D0E6";
	private final static String ColorDesignStatus = "#F3FCF8";
	private final static String ColorDesignDoneStatus = "#FADFAD";
	private final static String ColorProductionStatus = "#EBC7DF";
	private final static String ColorDeliveryStatus = "#D0F0C0";
	private final static String ColorFinishStatus = "#dfe300";

	@Autowired
	public InitDB(UserService userService, RoleService roleService, PaymentService paymentService,
		StatusService statusService, DeliveryService deliveryService, OrderService orderService,
		CustomerService customerService, ItemService itemService, DeliveryTypeService deliveryTypeService,
		FuncMenuService functionService, ProductService productService, PhoneModelService phoneModelService,
		MailService mailService) {
		this.userService = userService;
		this.roleService = roleService;
		this.paymentService = paymentService;
		this.statusService = statusService;
		this.deliveryTypeService = deliveryTypeService;
		this.functionService = functionService;
	}

	@PostConstruct
	public void createDB() throws ParseException {

		//DeliveryType//
		DeliveryType toAddress = new DeliveryType("до адреса", false);
		DeliveryType pickup = new DeliveryType("самовывоз", true);
		deliveryTypeService.save(toAddress);
		deliveryTypeService.save(pickup);
		//Status//
		Status status1 = new Status("новый", 1L, ColorNewStatus);
		statusService.save(status1);
		Status status2 = new Status("дизайн", 2L, ColorDesignStatus);
		statusService.save(status2);
		Status status3 = new Status("дизайн готов", 3L, ColorDesignDoneStatus);
		statusService.save(status3);
		Status status4 = new Status("производство", 4L, ColorProductionStatus);
		statusService.save(status4);
		Status status5 = new Status("доставка", 5L, ColorDeliveryStatus);
		statusService.save(status5);
		Status status6 = new Status("закончен", 6L, ColorFinishStatus);
		statusService.save(status6);
		//Роли//
		HashSet<Status> allStatus = new HashSet<>();
		allStatus.add(status1);
		allStatus.add(status2);
		allStatus.add(status3);
		allStatus.add(status4);
		allStatus.add(status5);
		allStatus.add(status6);
		//Функции ролей
		FuncMenu managerFunc = new FuncMenu("Главная: менеджер", "/manager");
		functionService.save(managerFunc);
		FuncMenu bossFunc = new FuncMenu("Главная: директор", "/director");
		functionService.save(bossFunc);
		FuncMenu bossCustomerFunc = new FuncMenu("Клиенты", "/director/customers/");
		functionService.save(bossCustomerFunc);
		FuncMenu bossStuffFunc = new FuncMenu("Персонал", "/director/stuff/");
		functionService.save(bossStuffFunc);
		FuncMenu bossPanelFunc = new FuncMenu("Панель управления", "/director/controlpanel/statuses");
		functionService.save(bossPanelFunc);
		FuncMenu bossStatistic = new FuncMenu("Статистика", "/director/statistic/middle/");
		functionService.save(bossStatistic);
		FuncMenu designerDashboardFunction = new FuncMenu("Главная: дизайнер", "/designer");
		functionService.save(designerDashboardFunction);
		FuncMenu masterDashboardFunction = new FuncMenu("Главная: мастер", "/master");
		functionService.save(masterDashboardFunction);
		//Роли
		Role role1 = new Role("MANAGER", "/manager/", allStatus);
		role1.getFunctions().add(managerFunc);
		//role1.getFunctions().add(managerCustomerFunc);
		roleService.save(role1);
		Role role2 = new Role("DESIGNER", "/designer/", status2);
		role2.getFunctions().add(designerDashboardFunction);
		roleService.save(role2);
		Role role3 = new Role("MASTER", "/master/", status4);
		role3.getFunctions().add(masterDashboardFunction);
		roleService.save(role3);
		Role boss = new Role("BOSS", "/director/", allStatus);
		boss.getFunctions().add(bossFunc);
		boss.getFunctions().add(bossCustomerFunc);
		boss.getFunctions().add(bossPanelFunc);
		boss.getFunctions().add(bossStuffFunc);
		boss.getFunctions().add(bossStatistic);
		roleService.save(boss);
		//Пользователи//
		User user4 = new User("boss", "Director", "Boss", false, false, boss, "arcas.llc@yandex.ru",
			"+7-123-456-78-94");
		userService.setPasswordEncoder(user4, "123");
		userService.save(user4);
		//Payment//
		Payment payment1 = new Payment("Cash", true);
		paymentService.save(payment1);
		Payment payment2 = new Payment("Visa", false);
		paymentService.save(payment2);
		Payment payment3 = new Payment("наложный платеж", false);
		paymentService.save(payment3);

	}
}
