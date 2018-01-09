package main.config;

import java.util.List;
import main.model.Role;
import main.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserSuccessHandler userSuccessHandler;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		List<Role> roleList = roleService.getAll();
		http.csrf().disable().addFilterBefore(filter, CsrfFilter.class);
		http.authorizeRequests().antMatchers("/").authenticated().
			and().formLogin().successHandler(userSuccessHandler).loginPage("/login").
			and().exceptionHandling().accessDeniedPage("/403");
		for (int i = 0; i < roleList.size(); i++) {
			if (!roleList.get(i).getUrl().equals("/")) {
				String url = roleList.get(i).getUrl() + "**";
				http.authorizeRequests().antMatchers(url).hasAnyAuthority(roleList.get(i).getName(), "BOSS");
			}
		}
	}
}
