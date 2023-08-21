package com.ctel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.ctel.filter.SecurityFilter;

@Configuration
@EnableWebSecurity
public class WebsecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private InvalidUserAuthEntryPoint authenticationEntryPoint;

	@Autowired
	private SecurityFilter securityFilter;

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/*").allowedOrigins("*").allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
						"Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
//				.allowCredentials(true)
//				.maxAge(3600)
		;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// disabling Cross-Origin Resource Server
		// http.cors().disable();
		http.cors();
		// disabling Cross-Site Request Forgery
		http.csrf().disable().
		// no authentication required
				authorizeRequests().antMatchers("/saveCust", "/login", "/updateCust/{cid}", "/viewCust/{cid}","/viewCusts","/deleteCust/{cid}")
				.permitAll()

				// authority required

//				.antMatchers("/updateCust/{cid}", "/viewCust/{cid}").access("hasAuthority('admin') OR @userSecurity.hasUserId(authentication,#cid)")

//				.antMatchers("/saveInv", "/deleteInvItem/{iid}", "/updateInvItem/{iid}").hasAnyAuthority("seller", "admin")
//				.antMatchers("/viewInvItems","/viewInvItems/{iid}").hasAnyAuthority("customer","seller", "admin")
//				
//				.antMatchers("/viewProds", "/viewProd/{pid}").hasAnyAuthority("seller", "customer", "admin")
//				
//				.antMatchers( "/viewSlr/{sid}", "/updateSlr/{sid}").hasAnyAuthority("seller", "admin")
//				
//				.antMatchers("/saveOrd/{cid}","/viewOrd/{cid}", "/orderInvoice/pdf/{oid}").hasAnyAuthority("customer", "admin")
//				.antMatchers("/saveOrd/{cid}","/viewOrd/{cid}").access("@userSecurity.hasUserId(authentication,#cid)")

				.anyRequest().hasAuthority("admin").and().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

				// filter from 2nd request onwards
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
