package com.ctel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ctel.model.Inventory;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Integer> {
	
	@Query(value = "SELECT * FROM inventory i WHERE i.prod_id = ?1", nativeQuery = true)
		Inventory findProdId(Integer prodId);
	@Query(value = "SELECT * FROM inventory i WHERE i.seller_id = ?1", nativeQuery = true)
		Inventory findSellerId(Integer sellerId);
	@Query(value = "SELECT * FROM inventory i WHERE i.seller_id = ?1 and i.prod_id = ?2", nativeQuery = true)
		Inventory findAllBySellerIdAndProdId(Integer sellerId, Integer prodId);
}