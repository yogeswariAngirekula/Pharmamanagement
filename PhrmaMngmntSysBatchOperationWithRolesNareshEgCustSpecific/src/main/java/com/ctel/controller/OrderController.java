package com.ctel.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctel.model.Order;
import com.ctel.repository.OrderRepo;
import com.ctel.service.OrderService;

@RestController
public class OrderController {

	@Autowired
	private OrderRepo userDao;

	@Autowired
	private OrderService ordService;

	@PreAuthorize("@userSecurity.hasUserId(authentication,#cid)")
	@PostMapping("/saveOrd/{cid}")
	public ResponseEntity<?> saveUser(@RequestBody Order ord, @PathVariable Integer cid) {
		List<String> s = ordService.orderValidator(ord);
		return ResponseEntity.ok().body(s);
	}

	@GetMapping("/viewOrds")
	public ResponseEntity<?> getUser() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<Order> ordList = (List<Order>) userDao.findAll();
		if (!ordList.isEmpty()) {
			map.put("status", 1);
			map.put("data", ordList);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/viewOrd/{cid}")
	public ResponseEntity<?> getUserById(@PathVariable Integer cid) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		try {
			List<Order> viewOrds = userDao.findByCustId(cid);
			map.put("status", 1);
			map.put("data", viewOrds);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception ex) {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/deleteOrd/{oid}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer oid) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Order ord = userDao.findById(oid).get();
			userDao.delete(ord);
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

	@PutMapping("/updateOrd/{oid}")
	public ResponseEntity<?> updateUserById(@PathVariable Integer oid, @RequestBody Order ordDetail) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Optional<Order> resOrd = userDao.findById(oid);

		if (resOrd.isPresent() == true) {
			Order ord = resOrd.get();

			if (ordDetail.getPdtList() != null)
				ord.setPdtList(ordDetail.getPdtList());
			if (ordDetail.getCid() != null)
				ord.setCid(ordDetail.getCid());
			if (ordDetail.getBill() != null)
				ord.setBill(ordDetail.getBill());
			if (ordDetail.getSellerId() != null)
				ord.setSellerId(ordDetail.getSellerId());
			if (ordDetail.getCreatedDate() != null)
				ord.setCreatedDate(ordDetail.getCreatedDate());

			userDao.save(ord);
			map.put("status", 1);
			map.put("data", userDao.findById(oid));
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}                                            

}