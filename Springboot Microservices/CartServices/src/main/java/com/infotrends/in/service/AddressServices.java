package com.infotrends.in.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotrends.in.dao.AddressRepository;
import com.infotrends.in.data.AddressDetails;

@Service
public class AddressServices {

	@Autowired
	private AddressRepository addressRepo;

	@Autowired
	private SequenceGeneratorService sequenceGenSvc;
	
	public AddressDetails save(AddressDetails details) {
		details.setAddressId(sequenceGenSvc.generateSequence("Address"));
		return addressRepo.save(details);
	}
	

	public AddressDetails update(AddressDetails details) {
		return addressRepo.save(details);
	}
	
	public AddressDetails findByAddrId(int id) {
		return addressRepo.findByAddressId(id);
	}
	
	public List<AddressDetails> findByCustId(int id) {
		return addressRepo.findByCustId(id);
	}
	
	public boolean deleteAddress(int id) {
		AddressDetails data = addressRepo.findByAddressId(id);
		if(data!=null && data.getAddressId()>0) {
			addressRepo.delete(data);
			return true;
		}
		return false;
	}
	
}
