package main.config;

import main.model.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

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

	@Autowired
	public InitDB(UserService userService, RoleService roleService, PaymentService paymentService,
				  StatusService statusService, DeliveryService deliveryService,
				  OrderService orderService, CustomerService customerService,
				  ItemService itemService, DeliveryTypeService deliveryTypeService,
				  FuncMenuService functionService) {
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
	}

	@PostConstruct
	public void createDB() throws ParseException {
		//DeliveryType//
		DeliveryType toAddress = new DeliveryType("address", false);
		DeliveryType pickup = new DeliveryType("pickup", true);
		deliveryTypeService.save(toAddress);
		deliveryTypeService.save(pickup);
		//Status//
		Status status1 = new Status("new", 1L, "#C5D0E6");
		statusService.save(status1);
		Status status2 = new Status("design", 2L, "#F3FCF8");
		statusService.save(status2);
		Status status3 = new Status("design done", 3L, "#FADFAD");
		statusService.save(status3);
		Status status4 = new Status("production", 4L, "#EBC7DF");
		statusService.save(status4);
		Status status5 = new Status("delivery", 5L, "#D0F0C0");
		statusService.save(status5);
		Status status6 = new Status("finish", 6L, "dfe3");
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
		FuncMenu managerCustomerFunc = new FuncMenu("Customers", "/manager/customers/");
		functionService.save(managerCustomerFunc);
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
		role1.getFunctions().add(managerCustomerFunc);
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
		User user1 =
			new User("manager", "123", "Manager", "Tramp", false, false, role1, "putin@kremlin.ru",
				"123");
		userService.save(user1);
		User user2 = new User("designer", "123", "Designer", "Gucci", false, false, role2,
			"medvedev@kremlin.ru", "321");
		userService.save(user2);
		User user3 =
			new User("master", "123", "Master", "Carlo", false, false, role3, "pupkin@kremlin.ru",
				"456");
		userService.save(user3);
		User user4 =
			new User("boss", "123", "Director", "Boss", false, false, boss, "vasya@kremlin.ru",
				"654");
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
		Customer customer1 =
			new Customer("Piter", "Parker", "spider@mail.ru", "911", delivery1, "Russia",
				"saint-Petersburg", "sizam street", "777");
		customerService.save(customer1);
		Customer customer2 =
			new Customer("Bruce", "Wayne", "batman@mail.ru", "002", delivery2, "Kazahstan",
				"Almata", "bumbum street", "999");
		customerService.save(customer2);
		//Item//
		Item item1 = new Item("Case", "iphone 10", "metall", "my comment...", 1, 100d, false);
		Item item2 = new Item("Case", "iphone 7", "wood", "my comment...", 2, 200d, false);
		Item item3 = new Item("Case", "iphone 8", "plastic", "my comment...", 1, 100d, false);
		Item item4 = new Item("Case", "iphone 5", "wood", "my comment...", 2, 200d, false);
		Item item5 = new Item("Case", "Nexus 6P", "wood", "через гравировка", 5, 230d, false);
		Item item6 =
			new Item("Case", "Samsung Galaxy", "plactic", "матовый пластик найдешь на складе", 3,
				130d, false);
		Item item7 =
			new Item("Case", "IPhoneX", "metal", "метал должен быть матовым", 1, 350d, false);
		//Order//
		Date createDate = new Date();
		String date1 = "03/10/2017";
		String date2 = "10/10/2017";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date d1 = dateFormat.parse(date1);
		Date d2 = dateFormat.parse(date2);
		Order order1 =
			new Order("1", false, false, createDate, toAddress, payment1, status1, customer1, item1,
				user1, user2, user3);
		orderService.save(order1);
		Order order2 =
			new Order("2", false, false, createDate, toAddress, payment2, status2, customer2, item2,
				user1, user2, user3);
		orderService.save(order2);
		Order order3 =
			new Order("3", false, false, createDate, pickup, payment1, status3, customer2, item3,
				user1, user2, user3, deliveryPickup2);
		orderService.save(order3);
		Order order4 =
			new Order("4", false, false, createDate, pickup, payment2, status4, customer1, item4,
				user1, user2, user3, deliveryPickup);
		orderService.save(order4);
		Order order5 =
			new Order("5", false, false, d1, toAddress, payment2, status3, customer1, item5, user1,
				user2, user3);
		orderService.save(order5);
		Order order6 =
			new Order("6", false, false, d2, toAddress, payment2, status3, customer1, item6, user1,
				user2, user3);
		orderService.save(order6);
	}
}
