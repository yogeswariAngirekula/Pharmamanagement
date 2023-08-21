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
import org.springframework.web.bind.annotation.RestController;

import com.ctel.model.Inventory;
import com.ctel.repository.InventoryRepo;
import com.ctel.service.InventoryService;

@RestController
public class InventoryController {

	@Autowired
	private InventoryRepo userDao;

	@Autowired
	private InventoryService invService;

	@PostMapping(value = "saveInv", consumes = { "application/json" })
	public ResponseEntity<?> saveUser(@RequestBody Inventory inv) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Boolean b = invService.invValidator(inv);
		if (b) {
			map.put("status", 1);
			map.put("message", "Record is Saved Successfully!");
			return new ResponseEntity<>(map, HttpStatus.CREATED);
		} else {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/viewInvItems")
	public ResponseEntity<?> getUser() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<Inventory> invList = (List<Inventory>) userDao.findAll();
		if (!invList.isEmpty()) {
			map.put("status", 1);
			map.put("data", invList);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/viewInvItems/{iid}")
	public ResponseEntity<?> getUserById(@PathVariable Integer iid) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Optional<Inventory> viewInvItem = userDao.findById(iid);
			map.put("status", 1);
			map.put("data", viewInvItem);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception ex) {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/deleteInvItem/{iid}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer iid) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Inventory inv = userDao.findById(iid).get();
			userDao.delete(inv);
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

	@PutMapping("/updateInvItem/{iid}")
	public ResponseEntity<?> updateUserById(@PathVariable Integer iid, @RequestBody Inventory invItemDetail) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		Optional<Inventory> resInvItem = userDao.findById(iid);

		if (resInvItem.isPresent() == true) {
			Inventory invItem = resInvItem.get();

			if (invItemDetail.getProdName() != null)
				invItem.setProdName(invItemDetail.getProdName());
			if (invItemDetail.getProdId() != null)
				invItem.setProdId(invItemDetail.getProdId());
			if (invItemDetail.getSellerId() != null)
				invItem.setSellerId(invItemDetail.getSellerId());
			if (invItemDetail.getStockOut() != null)
				invItem.setStockOut(invItemDetail.getStockOut());
			if (invItemDetail.getRecordedDate() != null)
				invItem.setRecordedDate(invItemDetail.getRecordedDate());
			if (invItemDetail.getQty() != null)
				invItem.setQty(invItemDetail.getQty());

			userDao.save(invItem);
			map.put("status", 1);
			map.put("data", userDao.findById(iid));
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			map.clear();
			map.put("status", 0);
			map.put("message", "Data is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

}