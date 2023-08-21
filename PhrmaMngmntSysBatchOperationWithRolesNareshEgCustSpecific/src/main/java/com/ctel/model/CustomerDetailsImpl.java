package com.ctel.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomerDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Customer cust;

	public CustomerDetailsImpl(Customer cust) {
		super();
		this.cust = cust;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.cust.getPassword();
	}

	@Override
	public String getUsername() {
		return this.cust.getEmailId();
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}

	public Customer getUserDetails() {
		return cust;
	}

}
