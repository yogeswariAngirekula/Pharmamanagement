package com.ctel.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ctel.model.Customer;
import com.ctel.repository.CustomerRepo;
import com.ctel.service.CustomerService;

@CrossOrigin(origins = "*")
@RestController
public class CustomerController {

	@Autowired
	CustomerRepo userDao;

	@Autowired
	CustomerService custServ;

	@PostMapping("/saveCust")
	public ResponseEntity<?> saveUser(@RequestBody Customer customer) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String s = custServ.registerValidator(customer);
		map.put("status", 1);
		map.put("message", s);
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}

	@PostMapping("/saveCusts")
	public ResponseEntity<?> saveUsers(@RequestBody Customer customer) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String s = custServ.registerValidator(customer);
		map.put("status", 1);
		map.put("message", s);
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}

//	@GetMapping("/viewCusts")
//	public ResponseEntity<?> getUser() {
//		Map<String, Object> map = new LinkedHashMap<String, Object>();
//		List<Customer> userList = (List<Customer>) userDao.findAll();
//		if (!userList.isEmpty()) {
//			map.put("status", 1);
//			map.put("data", userList);
//			return new ResponseEntity<>(map, HttpStatus.OK);
//		} else {
//			map.clear();
//			map.put("status", 0);
//			map.put("message", "Data is not found");
//			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
//		}
//	}

	@GetMapping("/viewCusts")
	public List<Customer> getUser() {
		// Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<Customer> userList = (List<Customer>) userDao.findAll();
		if (userList != null) {
			return userList;
		} else {
			return null;
		}
	}

	@PreAuthorize("@userSecurity.hasUserId(authentication,#cid)")
	@GetMapping("/viewCust/{cid}")
	public ResponseEntity<?> getUserById(@PathVariable Integer cid, Authentication authentication) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Optional<Customer> viewCust = userDao.findById(cid);
			map.put("status", 1);
			map.put("data", viewCust);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception ex) {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/deleteCust/{cid}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer cid) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Customer cust = userDao.findById(cid).get();
			userDao.delete(cust);
			map.put("status", 1);
			map.put("message", "Record is deleted successfully!");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception ex) {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("@userSecurity.hasUserId(authentication,#cid)")
	@PutMapping("/updateCust/{cid}")
	public ResponseEntity<?> updateUserById(@PathVariable Integer cid, @RequestBody Customer custDetail,
			Authentication authentication) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		Boolean b = custServ.updCustValidator(cid, custDetail);

		if (b == true) {
			map.put("status", 1);
			map.put("data", userDao.findById(cid));
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

}