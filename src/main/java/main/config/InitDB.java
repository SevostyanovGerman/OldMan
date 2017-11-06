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

	@Autowired
	public InitDB(UserService userService, RoleService roleService, PaymentService paymentService,
				  StatusService statusService, DeliveryService deliveryService, OrderService orderService,
				  CustomerService customerService, ItemService itemService) {
		this.userService = userService;
		this.roleService = roleService;
		this.paymentService = paymentService;
		this.statusService = statusService;
		this.deliveryService = deliveryService;
		this.orderService = orderService;
		this.customerService = customerService;
		this.itemService = itemService;
	}

	@PostConstruct
	public void createDB() throws ParseException {
		//Status//
		Status status1 = new Status("new", 1l);
		statusService.save(status1);
		Status status2 = new Status("design", 2l);
		statusService.save(status2);
		Status status3 = new Status("design done", 3l);
		statusService.save(status3);
		Status status4 = new Status("production", 4l);
		statusService.save(status4);
		Status status5 = new Status("delivery", 5l);
		statusService.save(status5);
		Status status6 = new Status("finish", 6l);
		statusService.save(status6);
		//Роли//
		HashSet <Status> allStatus = new HashSet <>();
		allStatus.add(status1);
		allStatus.add(status2);
		allStatus.add(status3);
		allStatus.add(status4);
		allStatus.add(status5);
		allStatus.add(status6);
		Role role1 = new Role("MANAGER", "/manager/", allStatus);
		roleService.save(role1);
		Role role2 = new Role("DESIGNER", "/designer/", status2);
		roleService.save(role2);
		Role role3 = new Role("MASTER", "/master/", status3);
		roleService.save(role3);
		Role role4 = new Role("BOSS", "/director/", allStatus);
		roleService.save(role4);
		//Пользователи//
		User user1 = new User("manager", "123", "Donald", "Tramp", false, false, role1, "putin@kremlin.ru", "123");
		userService.save(user1);
		User user2 = new User("designer", "123", "GUCCIO", "GUCCI", false, false, role2, "medvedev@kremlin.ru", "321");
		userService.save(user2);
		User user3 = new User("master", "123", "Papa", "Carlo", false, false, role3, "pupkin@kremlin.ru", "456");
		userService.save(user3);
		User user4 = new User("boss", "123", "Hugo", "Boss", false, false, role4, "vasya@kremlin.ru", "654");
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
		//Customer//
		Customer customer1 = new Customer("Piter", "Parker", "spider@mail.ru", "911", delivery1);
		customerService.save(customer1);
		Customer customer2 = new Customer("Bruce", "Wayne", "batman@mail.ru", "002", delivery2);
		customerService.save(customer2);
		//Item//
		Item item1 = new Item("Case", "iphone 10", "metall", "my comment...", 1, 100d, false);
		itemService.save(item1);
		Item item2 = new Item("Case", "iphone 7", "wood", "my comment...", 2, 200d, false);
		itemService.save(item2);
		Item item3 = new Item("Case", "iphone 8", "plastic", "my comment...", 1, 100d, false);
		itemService.save(item3);
		Item item4 = new Item("Case", "iphone 5", "wood", "my comment...", 2, 200d, false);
		itemService.save(item4);
		Item item5 = new Item("Case", "Nexus 6P", "wood", "через гравировка", 5, 230d, false);
		itemService.save(item5);
		Item item6 = new Item("Case", "Samsung Galaxy", "plactic", "матовый пластик найдешь на складе", 3, 130d, false);
		itemService.save(item6);
		Item item7 = new Item("Case", "IPhoneX", "metal", "метал должен быть матовым", 1, 350d, false);
		itemService.save(item7);
		//Order//
		Date createDate = new Date();
		String date1 = "03/10/2017";
		String date2 = "10/10/2017";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date d1 = dateFormat.parse(date1);
		Date d2 = dateFormat.parse(date2);

		Order order1 =
			new Order("1", false, false, 100d, createDate, "courier", payment1, status1, customer1, item1, user1, user2,
				user3);
		orderService.save(order1);
		Order order2 =
			new Order("2", false, false, 100d, createDate, "courier", payment2, status2, customer2, item2, user1, user2,
				user3);
		orderService.save(order2);
		Order order3 =
			new Order("3", false, false, 100d, createDate, "courier", payment1, status3, customer2, item3, user1, user2,
				user3);
		orderService.save(order3);
		Order order4 =
			new Order("4", false, false, 100d, createDate, "courier", payment2, status4, customer1, item4, user1, user2,
				user3);
		orderService.save(order4);
		Order order5 =
			new Order("5", false, false, 200d, d1, "take away", payment2, status3, customer1, item5, user1, user2,
				user3);
		orderService.save(order5);
		Order order6 =
			new Order("6", false, false, 350d, d2, "take away", payment2, status3, customer1, item6, user1, user2,
				user3);
		orderService.save(order6);
	}
}
