package com.ctel.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ctel.model.Customer;
import com.ctel.model.Order;
import com.ctel.model.Orderproduct;
import com.ctel.model.Product;
import com.ctel.model.Seller;
import com.ctel.repository.CustomerRepo;
import com.ctel.repository.OrderRepo;
import com.ctel.repository.OrderproductRepo;
import com.ctel.repository.ProductRepo;
import com.ctel.repository.SellerRepo;
import com.ctel.service.OrderPDFExporter;
import com.lowagie.text.DocumentException;

@Controller
public class OrderPDFController {

	@Autowired
	private OrderRepo ordRepo;

	@Autowired
	private OrderproductRepo ordPdtRepo;

	@Autowired
	private CustomerRepo custRepo;

	@Autowired
	private SellerRepo slrRepo;

	@Autowired
	private ProductRepo prodRepo;

	@GetMapping("/orderInvoice/pdf/{oid}")
	public String exportToPDF(HttpServletResponse response, @PathVariable Integer oid)
			throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		System.out.println(oid);

		Order ord = ordRepo.findById(oid).get();

		List<Orderproduct> ordPdtList = ordPdtRepo.findByOid(ord.getOid());

		Customer cust = custRepo.findById(ord.getCid()).get();

		Seller slr = slrRepo.findById(ord.getSellerId()).get();

		List<Product> pdtList = new ArrayList<Product>();

		for (Orderproduct temp : ordPdtList) {
			pdtList.add(prodRepo.findById(temp.getPid()).get());
		}

		OrderPDFExporter exporter = new OrderPDFExporter(ord, ordPdtList, cust, slr, pdtList);
		exporter.export(response);
		return "Your Invoice is generated";

	}
}