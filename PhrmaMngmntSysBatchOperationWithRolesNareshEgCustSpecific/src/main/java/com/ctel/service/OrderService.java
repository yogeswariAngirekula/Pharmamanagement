package com.ctel.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctel.model.Order;
import com.ctel.model.Orderproduct;
import com.ctel.repository.InventoryRepo;
import com.ctel.repository.OrderRepo;
import com.ctel.repository.OrderproductRepo;
import com.ctel.repository.ProductRepo;

@Service
public class OrderService {

	@Autowired
	private OrderRepo ordRepo;

	@Autowired
	private OrderproductRepo ordProdRepo;

	@Autowired
	private InventoryRepo invRepo;

	@Autowired
	private ProductRepo prodRepo;

	public List<String> orderValidator(Order ord) {
		Integer countElse = 0;
		Integer countIf = 0;

		List<String> op = new ArrayList<String>();

		Double bill = 0.0;

		List<Orderproduct> list = ord.getPdtList();

		for (Orderproduct l : list) {

			// Checking Whether Product with ProdId is available form Seller Id in Inventory
			// ==> If exists get its Qty

			if (invRepo.findAllBySellerIdAndProdId(ord.getSellerId(), l.getPid()) != null
					&& invRepo.findAllBySellerIdAndProdId(ord.getSellerId(), l.getPid()).getQty() > l.getQty()) {

				System.out.println("Required Order Quantity is available in Inventory");

				// Calculating Bill and Printing Invoice and Updating Order Repo

				l.setPrice(l.getQty() * prodRepo.findById(l.getPid()).get().getPrice());

				l.setProductName(prodRepo.findById(l.getPid()).get().getProdName());

				l.setOrderTemp(ord);

				ordProdRepo.save(l);

				bill = bill + (l.getQty() * prodRepo.findById(l.getPid()).get().getPrice());

				// Calculating StockOut and Quantity and Updating Inventory Repo

				invRepo.findAllBySellerIdAndProdId(ord.getSellerId(), l.getPid()).setStockOut(l.getQty().intValue()
						+ invRepo.findAllBySellerIdAndProdId(ord.getSellerId(), l.getPid()).getStockOut());
				invRepo.findAllBySellerIdAndProdId(ord.getSellerId(), l.getPid()).setQty(
						invRepo.findAllBySellerIdAndProdId(ord.getSellerId(), l.getPid()).getQty() - l.getQty());
				invRepo.findAllBySellerIdAndProdId(ord.getSellerId(), l.getPid()).setRecordedDate(LocalDateTime.now());

				invRepo.save(invRepo.findAllBySellerIdAndProdId(ord.getSellerId(), l.getPid()));
				countIf++;
				op.add("Invoice generated for product with productId: " + l.getPid());
			} else {
				countElse++;
				op.add("Ordered product with productId: " + l.getPid()
						+ " is not available from the requested seller / The required quantity : " + l.getQty()
						+ " is not available in the inventory of the requested seller");
				continue;
			}
		}
		System.out.println(
				"Invoice generated for : " + countIf + " no of products out of total : " + list.size() + " products");
		ord.setCreatedDate(LocalDateTime.now());
		ord.setBill(bill);
		ordRepo.save(ord);

		if (countIf > 0)
			op.add("Invoice generated for : " + countIf + " products out of total : " + list.size() + " products");

		return op;
	}
}