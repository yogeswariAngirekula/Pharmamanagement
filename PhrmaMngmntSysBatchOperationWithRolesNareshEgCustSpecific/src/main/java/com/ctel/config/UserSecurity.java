package com.ctel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ctel.repository.CustomerRepo;
import com.ctel.repository.InventoryRepo;
import com.ctel.repository.SellerRepo;

@Component("userSecurity")
public class UserSecurity {

	@Autowired
	private CustomerRepo custRepo;

	@Autowired
	private SellerRepo slrRepo;

	@Autowired
	private InventoryRepo userDao;

	public boolean hasUserId(Authentication authentication, Integer cid) {

		int resCid = custRepo.findByEmailId(authentication.getName()).getCid();
//		System.out.println(userId+"  "+userID);
		if (resCid == cid)
			return true;

		return false;
	}

}
