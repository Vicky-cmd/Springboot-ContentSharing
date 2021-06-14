package com.infotrends.in.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.infotrends.in.dao.CartRepository;
import com.infotrends.in.data.CartData;
import com.infotrends.in.data.ItemData;

@Service
public class CartDataServices {

	@Autowired
	private CartRepository cartrepo;
	
	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private SequenceGeneratorService sequenceGenSvc;
	
	public CartData save(CartData data) {
		data.setCartId(sequenceGenSvc.generateSequence("Cart"));
		return cartrepo.save(data);
	}

	public CartData update(CartData data) {
		return cartrepo.save(data);
	}
	
	public CartData findCartById(int id) {
		
		return cartrepo.findByCartId(id);
	}

	public CartData findByCartIdAndUserId(int cartId, int userId) {
		return cartrepo.findByCartIdAndUserId(cartId, userId);
	}

	
	public CartData getActiveCartForuser(int userId) {
		return cartrepo.findByStatusAndUserId("A", userId);
	}

	public CartData createNewCartForUser(int userId, ItemData item) {
		
		item.setItemId(1);
		CartData data = new CartData();
		data.setUserId(userId);
		data.setTotalAmount(item.getPrice());
		data.setTotQuantity(item.getQuantity());
		List<ItemData> itemsLst = new ArrayList<ItemData>();
		itemsLst.add(item);
		data.setCartItems(itemsLst);
		data.setTotalAmount(item.getPrice() * item.getQuantity());
		
		return save(data);
	}
	
	public List<CartData> findallCarts(int id) {
		
		return cartrepo.findByUserIdOrderByCartIdDesc(id);
	}
}
