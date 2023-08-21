package com.ctel.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ctel.config.JwtUtil;
import com.ctel.model.Customer;
import com.ctel.model.LoginRequest;
import com.ctel.service.CustomerService;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil util;

	@Autowired
	private CustomerService customerService;

	@PostMapping("/login")
	public HashMap<String, Object> loginUser(@RequestBody LoginRequest req) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(req.getEmailId(), req.getPassword()));
//token genaration...
		String accessToken = util.generateToken(req.getEmailId());

		Customer loginCust = customerService.findByEmailId(req.getEmailId());
		Integer loginCid = loginCust.getCid();

		Set<String> roles = loginCust.getRoles();
		Boolean isAdmin = false;
		String finalRole = "";
		for (String s : roles) {
			if (s.equals("admin")) {
				isAdmin = true;
				finalRole = s;
			} else {
				
				finalRole = s;
			}
		}
        System.out.println(finalRole);
		HashMap<String, Object> map = new HashMap<>();
		map.put("customer", loginCust);
		map.put("cid", loginCid);
		map.put("accessToken", accessToken);
		map.put("finalRole", finalRole);
		map.put("isAdmin", isAdmin);
		map.put("roles", loginCust.getRoles());

		return map;
	}

	@PostMapping("/welcome")
	public ResponseEntity<String> accessData(Principal p) {
		return ResponseEntity.ok("Hello User!!!" + p.getName());
	}

}