package com.ctel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ctel.model.Seller;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Integer> {
	
	@Query(value = "SELECT * FROM seller i WHERE i.emailId = ?1", nativeQuery = true)
	Seller findByEmailId(String emailId);
}