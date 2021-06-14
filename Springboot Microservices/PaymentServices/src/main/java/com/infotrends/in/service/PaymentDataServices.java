package com.infotrends.in.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotrends.in.dao.PaymentDataRepository;
import com.infotrends.in.data.PaymentData;

@Service
public class PaymentDataServices {
	
	@Autowired
	private PaymentDataRepository paymentRepo;

	@Autowired
	private SequenceGeneratorService sequenceGenSvc;
	
	public PaymentData save(PaymentData data) {
		int attemptId = sequenceGenSvc.generateSequence("payments");
		data.setAttemptId(attemptId);
		data.setReceiptId("RID000" + attemptId);
		return paymentRepo.save(data);
	}

	public PaymentData update(PaymentData data) {
		return paymentRepo.save(data);
	}

	public PaymentData findByAttemptId(int id) {
		return paymentRepo.findByAttemptId(id);
	}

	public PaymentData findByOrderId(String id) {
		return paymentRepo.findByOrderId(id);
	}

}
