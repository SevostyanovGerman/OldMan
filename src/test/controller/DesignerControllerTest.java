package controller;

import main.controller.DesignerController;
import main.model.Notification;
import main.model.Order;
import main.model.User;
import main.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DesignerControllerTest {

	@Mock
	private OrderService orderService;

	@Mock
	private UserService userService;

	@Mock
	private ItemService itemService;

	@Mock
	private ImageService imageService;

	@Mock
	private NotificationService notificationService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void designerServlets() {

		List<Order> allAllowed = new ArrayList<>();
		Order order1 = mock(Order.class);
		Order order2 = mock(Order.class);
		Order order3 = mock(Order.class);

		User currentUser = mock(User.class);

		allAllowed.add(order1);
		allAllowed.add(order2);
		allAllowed.add(order3);

		Notification notification1 = mock(Notification.class);
		Notification notification2 = mock(Notification.class);
		Notification notification3 = mock(Notification.class);
		List<Notification> notificationList1 = new ArrayList<>();
		notificationList1.add(notification1);
		notificationList1.add(notification2);
		notificationList1.add(notification3);

		Long idOrder = 1l;
		Long idNotification = 1l;

		when(userService.getCurrentUser()).thenReturn(currentUser);
		when(orderService.getAllAllowed(userService.getCurrentUser())).thenReturn(allAllowed);
		when(orderService.get(idOrder)).thenReturn(order1);
		when(notificationService.findAllByUser(currentUser.getName()))
			.thenReturn(notificationList1);

		DesignerController controller =
			new DesignerController(orderService, itemService, imageService, userService,
				notificationService);

//		Сервлет designer ( / )

		ModelAndView mav = controller.designer();

		Object c = mav.getModel().get("orders");
		Assert.assertEquals("не совпадают orders в return модели", allAllowed,
			mav.getModel().get("orders"));
		Assert.assertEquals("не совпадает имя шаблона", "/designerView/DesignerDashBoard",
			mav.getViewName());

//		Конец designer

//		Сервлет order "/designer/order/{id}"}

		mav = controller.order(idOrder);

		Assert.assertEquals("не совпадают order в return модели", order1,
			mav.getModel().get("order"));

		Assert.assertEquals("не совпадает имя шаблона", "/designerView/DesignerOrder",
			mav.getViewName());

	}

}

