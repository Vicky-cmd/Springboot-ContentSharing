package com.infotrends.in.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.infotrends.in.data.PaymentData;

@Repository
public interface PaymentDataRepository extends MongoRepository<PaymentData, String>{

	PaymentData findByAttemptId(int id);

	PaymentData findByOrderId(String id);

}
