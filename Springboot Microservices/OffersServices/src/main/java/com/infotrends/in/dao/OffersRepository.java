package com.infotrends.in.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.infotrends.in.data.OffersData;

@Repository
public interface OffersRepository extends MongoRepository<OffersData, String> {

	OffersData findByOfferId(int id);

	List<OffersData> findByCustId(int usrId);

	List<OffersData> findByItemId(int itemId);

	List<OffersData> findByItemIdAndStatus(int itemId, String string);

	List<OffersData> findByCustIdAndStatus(int usrId, String string);

}
