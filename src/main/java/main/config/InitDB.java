package main.config;

import main.model.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
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
	public void createDB() {
		//Status//
		Status status1 = new Status("new");
		statusService.save(status1);
		Status status2 = new Status("design");
		statusService.save(status2);
		Status status3 = new Status("design done");
		statusService.save(status3);
		Status status4 = new Status("production");
		statusService.save(status4);
		Status status5 = new Status("delivery");
		statusService.save(status5);
		Status status6 = new Status("finish");
		statusService.save(status6);
		//Роли//
		Role role1 = new Role("MANAGER", "/manager/");
		roleService.save(role1);
		Role role2 = new Role("DESIGNER", "/designer/");
		roleService.save(role2);
		Role role3 = new Role("MASTER", "/master/");
		roleService.save(role3);
		Role role4 = new Role("BOSS", "/boss/");
		roleService.save(role4);
		//Пользователи//
		HashSet <Status> allStatus = new HashSet <>();
		allStatus.add(status1);
		allStatus.add(status2);
		allStatus.add(status3);
		allStatus.add(status4);
		allStatus.add(status5);
		allStatus.add(status6);
		User user1 = new User("manager", "123", "Donald", "Tramp", 0, 0, role1);
		user1.setStatuses(allStatus);
		user1.getStatuses().add(status1);
		userService.save(user1);
		User user2 = new User("designer", "123", "GUCCIO", "GUCCI", 0, 0, role2);
		user2.getStatuses().add(status2);
		userService.save(user2);
		User user3 = new User("master", "123", "Papa", "Carlo", 0, 0, role3);
		user3.getStatuses().add(status4);
		userService.save(user3);
		User user4 = new User("boss", "123", "Hugo", "Boss", 0, 0, role4);
		user4.setStatuses(allStatus);
		userService.save(user4);
		//Payment//
		Payment payment1 = new Payment("cash");
		paymentService.save(payment1);
		Payment payment2 = new Payment("visa");
		paymentService.save(payment2);
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
		//Order//
		Date createDate = new Date();
		Order order1 =
			new Order("1", false, 100d, createDate, "courier", payment1, status1, customer1, item1, user1, user2,
				user3);
		orderService.save(order1);
		Order order2 =
			new Order("2", false, 100d, createDate, "courier", payment2, status2, customer2, item2, user1, user2,
				user3);
		orderService.save(order2);
		Order order3 =
			new Order("3", false, 100d, createDate, "courier", payment1, status3, customer2, item3, user1, user2,
				user3);
		orderService.save(order3);
		Order order4 =
			new Order("4", false, 100d, createDate, "courier", payment2, status4, customer1, item4, user1, user2,
				user3);
		orderService.save(order4);
	}
}
