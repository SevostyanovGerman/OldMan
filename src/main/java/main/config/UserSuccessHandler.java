package main.config;

import main.model.Role;
import main.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class UserSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private RoleService roleService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication)
		throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_OK);
		boolean check = false;
		List<Role> roleList = roleService.getAll();
		String url = "/";
		for (int i = 0; i < roleList.size(); i++) {
			for (GrantedAuthority auth : authentication.getAuthorities()) {
				if (auth.getAuthority().equals(roleList.get(i).getName())) {
					url = roleList.get(i).getUrl();
					check = true;
					break;
				}
				if (check) {
					break;
				}
			}
		}
		response.sendRedirect(url);
	}
}

