package com.infotrends.in.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.infotrends.in.data.CartData;

@Repository
public interface CartRepository extends MongoRepository<CartData, String>{

	
	CartData findByCartId(int id);
	
	CartData findByCartIdAndUserId(int cartId, int userId);
	
	CartData findByStatusAndUserId(String status, int cartId);
	
	List<CartData> findByUserId(int userId);
	
	List<CartData> findByUserIdOrderByCartIdDesc(int userId);
}
