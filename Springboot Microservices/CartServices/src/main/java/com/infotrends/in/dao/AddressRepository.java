package com.infotrends.in.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.infotrends.in.data.AddressDetails;

@Repository
public interface AddressRepository extends MongoRepository<AddressDetails, String> {

	public AddressDetails findByAddressId(int id);
	public List<AddressDetails> findByCustId(int custId);
}
