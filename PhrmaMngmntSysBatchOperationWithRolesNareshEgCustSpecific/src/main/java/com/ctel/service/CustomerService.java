package com.ctel.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ctel.model.Customer;
import com.ctel.repository.CustomerRepo;

@Service
public class CustomerService implements UserDetailsService {
	@Autowired
	private CustomerRepo custRepo;

	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	public String registerValidator(Customer cust) {

		// Encode Password
		cust.setPassword(pwdEncoder.encode(cust.getPassword()));

		if (!cust.getFirstName().isEmpty() && !cust.getLastName().isEmpty() && !cust.getAddress().isEmpty()
				&& cust.getContactNo() > 999999999 && cust.getContactNo() < 10000000000L && !cust.getCity().isEmpty()
				&& !cust.getState().isEmpty() && !cust.getCountry().isEmpty()
				&& (cust.getGender().equalsIgnoreCase("male") || cust.getGender().equalsIgnoreCase("female"))
				&& !cust.getEmailId().isEmpty() && cust.getPassword().length() > 8) {
			cust.setRoles(new HashSet<String>(Arrays.asList("customer")));
			System.out.println(cust.getRoles());
			custRepo.save(cust);
			return "Saved Successfully";
		} else
			return "Check the input Values : First Name & Last Name cant be null Invalid Phone Number/Email & password length should be > 8 chars";
	}

	public Customer findByEmailId(String emailId) {
		return custRepo.findByEmailId(emailId);
	}

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		Customer cust = custRepo.findByEmailId(emailId);

		if (cust == null) {
			System.out.println("exception thrown");
			throw new UsernameNotFoundException(emailId + "not found");
		}
		return new org.springframework.security.core.userdetails.User(emailId, cust.getPassword(),
				cust.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList()));
	}

//	public Boolean updCustValidator(Integer cid, Customer custDetail) {
//		// TODO Auto-generated method stub
//
//		Optional<Customer> resCust = custRepo.findById(cid);
//
//		if (resCust.isPresent() == true) {
//			Customer cust = resCust.get();
//			if (custDetail.getFirstName() != null)
//				cust.setFirstName(custDetail.getFirstName());
//			if (custDetail.getLastName() != null)
//				cust.setLastName(custDetail.getLastName());
//			if (custDetail.getAddress() != null)
//				cust.setAddress(custDetail.getAddress());
//			if (custDetail.getContactNo() != null)
//				cust.setContactNo(custDetail.getContactNo());
//			if (custDetail.getCity() != null)
//				cust.setCity(custDetail.getCity());
//			if (custDetail.getState() != null)
//				cust.setState(custDetail.getState());
//			if (custDetail.getCountry() != null)
//				cust.setCountry(custDetail.getCountry());
//			if (custDetail.getGender() != null)
//				cust.setGender(custDetail.getGender());
//			if (custDetail.getEmailId() != null)
//				cust.setEmailId(custDetail.getEmailId());
//			if (custDetail.getPassword() != null)
//				cust.setPassword(pwdEncoder.encode(custDetail.getPassword()));
//			if (custDetail.getRoles() != null)
//				cust.setRoles(custDetail.getRoles());
//
//			custRepo.save(cust);
//
//			return true;
//		} else
//			return false;
//	}

	public Boolean updCustValidator(Integer cid, Customer custDetail) {
		// TODO Auto-generated method stub

		Optional<Customer> resCust = custRepo.findById(cid);

		if (resCust.isPresent() == true) {
			Customer cust = resCust.get();
			custRepo.deleteById(cid);

			custDetail.setPassword(pwdEncoder.encode(cust.getPassword()));
			custDetail.setRoles(new HashSet<String>(Arrays.asList("customer")));

			custRepo.save(custDetail);

			return true;
		} else
			return false;
	}
}
