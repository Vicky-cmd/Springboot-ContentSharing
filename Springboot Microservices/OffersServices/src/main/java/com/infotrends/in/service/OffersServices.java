package com.infotrends.in.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotrends.in.dao.OffersRepository;
import com.infotrends.in.data.OffersData;

@Service
public class OffersServices {

	@Autowired
	private OffersRepository offersRepo;
	
	@Autowired
	private SequenceGeneratorService sequenceGenSvc;
	
	public OffersData save(OffersData data) {
		data.setOfferId(sequenceGenSvc.generateSequence("offers"));
		return offersRepo.save(data);
	}
	
	public OffersData update(OffersData data) {
		return offersRepo.save(data);
	}
	
	public OffersData findById(int id) {
		return offersRepo.findByOfferId(id);
	}
	
	public List<OffersData> findByUserId(int usrId) {
		return offersRepo.findByCustId(usrId);
	}

	public List<OffersData> findByItemId(int itemId) {
		return offersRepo.findByItemId(itemId);
	}

	public List<OffersData> findActiveOffersByUserId(int usrId) {
		return offersRepo.findByCustIdAndStatus(usrId, "A");
	}
	
	public List<OffersData> findActiveOffersByItemId(int itemId) {
		return offersRepo.findByItemIdAndStatus(itemId, "A");
	}
}
