package com.ctel.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctel.model.Inventory;
import com.ctel.repository.InventoryRepo;
import com.ctel.repository.ProductRepo;
import com.ctel.repository.SellerRepo;

@Service
public class InventoryService {

	@Autowired
	private InventoryRepo invRepo;

	@Autowired
	private ProductRepo prodRepo;

	@Autowired
	private SellerRepo slrRepo;

	public Boolean invValidator(Inventory inv) {
		Boolean b = false;

		if (prodRepo.findById(inv.getProdId()).get() != null && slrRepo.findById(inv.getSellerId()).get() != null) {
			System.out.println("ProductId & SellerId exists in Product and Seller Tables Resp");

			System.out.println("SellerId exists in both Seller and Inventory Tables");

			if (prodRepo.findById(inv.getProdId()).get().getPid() == inv.getProdId()
					&& slrRepo.findById(inv.getSellerId()).get().getSid() == inv.getSellerId()
					&& prodRepo.findById(inv.getProdId()).get().getProdName().equals(inv.getProdName())) {

				inv.setRecordedDate(LocalDateTime.now());

				invRepo.save(inv);
				b = true;
			}
		}
		return b;
	}
}