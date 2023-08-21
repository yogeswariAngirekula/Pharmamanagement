package com.ctel.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ctel.config.JwtUtil;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil util;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String token = request.getHeader("Authorization");

		if (token != null) {
			// TODO: Validation
			String userEmail = util.getEmailId(token);

			// username shouldnt be empty context-auth must be empty
			if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails usr = userDetailsService.loadUserByUsername(userEmail);
				// validate token
				boolean isValid = util.validateToken(token, usr.getUsername());

				if (isValid) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail,
							usr.getPassword(), usr.getAuthorities());
					// final object stored in SecurityContext with userdetails ==> email && pwd
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

		}
		filterChain.doFilter(request, response);

	}

}
