package main.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import javax.annotation.PostConstruct;
import main.model.Customer;
import main.model.Delivery;
import main.model.DeliveryType;
import main.model.FuncMenu;
import main.model.Item;
import main.model.Order;
import main.model.Payment;
import main.model.PhoneModel;
import main.model.Product;
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

	private DeliveryService deliveryService;

	private OrderService orderService;

	private CustomerService customerService;

	private ItemService itemService;

	private DeliveryTypeService deliveryTypeService;

	private FuncMenuService functionService;

	private ProductService productService;

	private PhoneModelService phoneModelService;

	private MailService mailService;

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
		this.deliveryService = deliveryService;
		this.orderService = orderService;
		this.customerService = customerService;
		this.itemService = itemService;
		this.deliveryTypeService = deliveryTypeService;
		this.functionService = functionService;
		this.productService = productService;
		this.phoneModelService = phoneModelService;
		this.mailService = mailService;
	}

	@PostConstruct
	public void createDB() throws ParseException {
//		Mail notificationMail = new Mail("NOTIFICATION", "Вам пришло новое сообщение", null,
//			"%s, Вам пришло новое сообщение, перейдите по ссылке чтобы его прочитать", "mail/mailNotification");
//		mailService.save(notificationMail);
//		mailService.save(resetPasswordMail);

		//DeliveryType//
		DeliveryType toAddress = new DeliveryType("address", false);
		DeliveryType pickup = new DeliveryType("pickup", true);
		deliveryTypeService.save(toAddress);
		deliveryTypeService.save(pickup);
		//Status//
		Status status1 = new Status("new", 1L, ColorNewStatus);
		statusService.save(status1);
		Status status2 = new Status("design", 2L, ColorDesignStatus);
		statusService.save(status2);
		Status status3 = new Status("design done", 3L, ColorDesignDoneStatus);
		statusService.save(status3);
		Status status4 = new Status("production", 4L, ColorProductionStatus);
		statusService.save(status4);
		Status status5 = new Status("delivery", 5L, ColorDeliveryStatus);
		statusService.save(status5);
		Status status6 = new Status("finish", 6L, ColorFinishStatus);
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
		FuncMenu managerFunc = new FuncMenu("Manager Dashboard", "/manager");
		functionService.save(managerFunc);
//		FuncMenu managerCustomerFunc = new FuncMenu("Customers", "/manager/customers/");
//		functionService.save(managerCustomerFunc);
		FuncMenu bossFunc = new FuncMenu("Boss Dashboard", "/director");
		functionService.save(bossFunc);
		FuncMenu bossCustomerFunc = new FuncMenu("Customers", "/director/customers/");
		functionService.save(bossCustomerFunc);
		FuncMenu bossStuffFunc = new FuncMenu("Stuff", "/director/stuff/");
		functionService.save(bossStuffFunc);
		FuncMenu bossPanelFunc = new FuncMenu("Panel", "/director/controlpanel/statuses");
		functionService.save(bossPanelFunc);
		FuncMenu bossStatistic = new FuncMenu("Statistic", "/director/statistic/middle/");
		functionService.save(bossStatistic);
		FuncMenu designerDashboardFunction = new FuncMenu("Designer Dashboard", "/designer");
		functionService.save(designerDashboardFunction);
		FuncMenu masterDashboardFunction = new FuncMenu("Master Dashboard", "/master");
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
		User user1 = new User("manager", "Manager", "Tramp", false, false, role1, "putin@kremlin.ru",
			"+7-123-456-78-91");
		userService.setPasswordEncoder(user1, "123");
		userService.save(user1);
		User user2 = new User("designer", "Designer", "Gucci", false, false, role2, "medvedev@kremlin.ru",
			"+7-123-456-78-92");
		userService.setPasswordEncoder(user2, "123");
		userService.save(user2);
		User user3 = new User("master", "Master", "Carlo", false, false, role3, "pupkin@kremlin.ru",
			"+7-123-456-78-93");
		userService.setPasswordEncoder(user3, "123");
		userService.save(user3);
		User user4 = new User("boss", "Director", "Boss", false, false, boss, "arcas.llc@yandex.ru",
			"+7-123-456-78-94");
		userService.setPasswordEncoder(user4, "123");
		userService.save(user4);
		//Payment//
		Payment payment1 = new Payment("Cash");
		paymentService.save(payment1);
		Payment payment2 = new Payment("Visa");
		paymentService.save(payment2);
		Payment payment3 = new Payment("Post");
		paymentService.save(payment3);
		//Delivery//
		Delivery delivery1 = new Delivery("Russia", "saint-Petersburg", "sizam street", "777");
		deliveryService.save(delivery1);
		Delivery delivery2 = new Delivery("Kazahstan", "Almata", "bumbum street", "999");
		deliveryService.save(delivery2);
		Delivery deliveryPickup = new Delivery("USA", "Springfield", "Gomer street", "123", true);
		deliveryService.save(deliveryPickup);
		Delivery deliveryPickup2 = new Delivery("Canada", "Toronto", "NHL street", "321", true);
		deliveryService.save(deliveryPickup2);
		//Customer//
		Customer customer1 = new Customer("Piter", "Parker", "spider@mail.ru", "911", delivery1);
		customerService.save(customer1);
		Customer customer2 = new Customer("Bruce", "Wayne", "batman@mail.ru", "002", delivery2);
		customerService.save(customer2);
		//Product//
		Product product1 = new Product("Case");
		productService.save(product1);
		Product product2 = new Product("Box");
		productService.save(product2);
		//Phone Model//
		PhoneModel phoneModel1 = new PhoneModel("iPhone X");
		phoneModelService.save(phoneModel1);
		PhoneModel phoneModel2 = new PhoneModel("Samsung One Note");
		phoneModelService.save(phoneModel2);
		PhoneModel phoneModel3 = new PhoneModel("Sony Experia Z");
		phoneModelService.save(phoneModel3);
		PhoneModel phoneModel4 = new PhoneModel("GOOGLE PIXEL 2");
		phoneModelService.save(phoneModel4);
		PhoneModel phoneModel5 = new PhoneModel("HTC U ULTRA");
		phoneModelService.save(phoneModel5);
		PhoneModel phoneModel6 = new PhoneModel("HUAWEI MATE 10");
		phoneModelService.save(phoneModel6);
		PhoneModel phoneModel7 = new PhoneModel("MEIZU MX7");
		phoneModelService.save(phoneModel7);
		//Item//
		Item item1 = new Item(product1, phoneModel1, "metal", "метал должен быть матовым", 1, 100d, false);
		Item item2 = new Item(product1, phoneModel2, "wood", "my comment...", 2, 200d, false);
		Item item3 = new Item(product2, phoneModel3, "plastic", "my comment...", 1, 100d, false);
		Item item4 = new Item(product2, phoneModel4, "wood", "my comment...", 2, 200d, false);
		Item item5 = new Item(product1, phoneModel5, "wood", "через гравировка", 5, 230d, false);
		Item item6 = new Item(product1, phoneModel6, "plastic", "матовый пластик найдешь на складе", 3, 130d, false);
		//Order//
		Date createDate = new Date();
		String date1 = "03/10/2017";
		String date2 = "10/10/2017";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date d1 = dateFormat.parse(date1);
		Date d2 = dateFormat.parse(date2);
		Order order1 = new Order("1", false, false, createDate, toAddress, payment1, status1, customer1, item1, user1,
			user2, user3);
		orderService.save(order1);
		Order order2 = new Order("2", false, false, createDate, toAddress, payment2, status2, customer2, item2, user1,
			user2, user3);
		orderService.save(order2);
		Order order3 = new Order("3", false, false, createDate, pickup, payment1, status3, customer2, item3, user1,
			user2, user3, deliveryPickup2);
		orderService.save(order3);
		Order order4 = new Order("4", false, false, createDate, pickup, payment2, status4, customer1, item4, user1,
			user2, user3, deliveryPickup);
		orderService.save(order4);
		Order order5 = new Order("5", false, false, d1, toAddress, payment2, status3, customer1, item5, user1, user2,
			user3);
		orderService.save(order5);
		Order order6 = new Order("6", false, false, d2, toAddress, payment2, status3, customer1, item6, user1, user2,
			user3);
		orderService.save(order6);
	}
}
