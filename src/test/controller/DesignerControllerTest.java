package controller;

import main.controller.DesignerController;
import main.model.Order;
import main.service.OrderService;
import main.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class TodoControllerTest {
	@InjectMocks

	DesignerController controller;

	@Mock
	OrderService orderService;

	@Mock
	UserService userService;



	@Before

	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testListPeopleInGroup() {
		orderService.getAllAllowed(userService.getCurrentUser());




		List<Order> orderList = new ArrayList<>();

		when(orderService.getAllAllowed(userService.getCurrentUser())).thenReturn(orderList);



		ModelMap modelMap = new ModelMap();

		ModelAndView viewName = controller.designer();

		assertEquals("/designerView/DesignerDashBoard", viewName.getViewName());
		System.out.println("MAAAAAAAXXXXXX");

		//assertThat(modelMap,  (Object) orderList);

	}

}

