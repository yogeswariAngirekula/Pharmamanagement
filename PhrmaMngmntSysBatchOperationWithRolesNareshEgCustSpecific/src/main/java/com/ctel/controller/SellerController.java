package com.ctel.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctel.model.Seller;
import com.ctel.repository.SellerRepo;
import com.ctel.service.SellerService;

@RestController
public class SellerController {

	@Autowired
	SellerRepo userDao;

	@Autowired
	SellerService slrServ;

	@PostMapping("/saveSeller")
	public ResponseEntity<?> saveUser(@RequestBody Seller slr) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String s = slrServ.registerValidator(slr);
		map.put("status", 1);
		map.put("message", s);
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}

	@GetMapping("/viewSellers")
	public ResponseEntity<?> getUser() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<Seller> slrList = (List<Seller>) userDao.findAll();
		if (!slrList.isEmpty()) {
			map.put("status", 1);
			map.put("data", slrList);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/viewSlr/{sid}")
	public ResponseEntity<?> getUserById(@PathVariable Integer sid) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Optional<Seller> viewSlr = userDao.findById(sid);
			map.put("status", 1);
			map.put("data", viewSlr);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception ex) {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/deleteSlr/{sid}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer sid) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Seller slr = userDao.findById(sid).get();
			userDao.delete(slr);
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

	@PutMapping("/updateSlr/{sid}")
	public ResponseEntity<?> updateUserById(@PathVariable Integer sid, @RequestBody Seller slrDetail) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		Optional<Seller> resSlr = userDao.findById(sid);

		if (resSlr.isPresent() == true) {
			Seller slr = resSlr.get();

			if (slrDetail.getSellerName() != null)
				slr.setSellerName(slrDetail.getSellerName());
			if (slrDetail.getStoreName() != null)
				slr.setStoreName(slrDetail.getStoreName());
			if (slrDetail.getAddress() != null)
				slr.setAddress(slrDetail.getAddress());
			if (slrDetail.getContactNo() != null)
				slr.setContactNo(slrDetail.getContactNo());
			if (slrDetail.getCity() != null)
				slr.setCity(slrDetail.getCity());
			if (slrDetail.getState() != null)
				slr.setState(slrDetail.getState());
			if (slrDetail.getCountry() != null)
				slr.setCountry(slrDetail.getCountry());
			if (slrDetail.getPanNo() != null)
				slr.setPanNo(slrDetail.getPanNo());
			if (slrDetail.getEmailId() != null)
				slr.setEmailId(slrDetail.getEmailId());
			if (slrDetail.getPassword() != null)
				slr.setPassword(slrDetail.getPassword());

			userDao.save(slr);
			map.put("status", 1);
			map.put("data", userDao.findById(sid));
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}

	}

}