package br.com.rafaelvieira.shopbeer.config;

import br.com.rafaelvieira.shopbeer.security.AppUserEmployeeDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = AppUserEmployeeDetailsService.class)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final UserDetailsService userDetailsService;

	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.requestMatchers("/layout/**")
				.requestMatchers("/images/**");
	}

	@Bean
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/cities/new").hasRole("REGISTER_CITY")
						.requestMatchers("/users/**").hasRole("REGISTER_USER")
						.anyRequest().authenticated()
				).formLogin((formLogin) -> formLogin
						.loginPage("/login")
						.permitAll()
				).logout((logout) -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				).sessionManagement((sessionManagement) -> sessionManagement
						.invalidSessionUrl("/login")
				);
	}

	/*
	 * metodo antigo de configurar o spring security deprecated
	 */
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//				.authorizeRequests()
//				.antMatchers("/cities/new").hasRole("REGISTER_CITY")
//				.antMatchers("/users/**").hasRole("REGISTER_USER")
//				.anyRequest().authenticated()
//				.and()
//				.formLogin()
//				.loginPage("/login")
//				.permitAll()
//				.and()
//				.logout()
//				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//				.and()
//				.sessionManagement()
//				.invalidSessionUrl("/login");
//	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}