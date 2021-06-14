package com.infotrends.in.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.infotrends.in.data.AddressDetails;
import com.infotrends.in.model.AddressModel;
import com.infotrends.in.model.ResponseModel;
import com.infotrends.in.service.AddressServices;

@Component
public class AddressOrc {

	@Autowired
	private AddressServices addressSvc;
	
	public ResponseEntity<ResponseModel> saveDetails(AddressModel data) throws Exception {
		ResponseModel resp = new ResponseModel();
		try {
			
			if(isDataValid(data)) {
				AddressDetails details = new AddressDetails(data);
				details = addressSvc.save(details);
				resp.setStatusCode(HttpStatus.CREATED.value());
				resp.setAddressId(resp.getAddressId());
				return new ResponseEntity<ResponseModel>(resp, HttpStatus.CREATED);
			} else {

				resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
				resp.setErrorMsg("Incorrect Address Details!");
				return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
			}
			
		}catch (Exception e) {
			
			resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.setErrorMsg("Internal Server Error Occured!");
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	public ResponseEntity<ResponseModel> updateDetails(int addressId,AddressModel data) throws Exception {
		ResponseModel resp = new ResponseModel();
		if(isDataValid(data)) {
			AddressDetails details = addressSvc.findByAddrId(addressId);
			details.setAddressDetails(data);
			details = addressSvc.save(details);
			resp.setStatusCode(HttpStatus.OK.value());
			resp.setAddressId(resp.getAddressId());
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.OK);
		} else {

			resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
			resp.setErrorMsg("Incorrect Address Details!");
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity<ResponseModel> deleteDetails(int addressId) throws Exception {
		ResponseModel resp = new ResponseModel();
		boolean isDeleted = addressSvc.deleteAddress(addressId);
		if(isDeleted) {
			resp.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.OK);
		} else {
			resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
			resp.setErrorMsg("Address Data Not Found!");
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	public ResponseEntity<AddressDetails> fetchAddressDetails(int addressId) throws Exception {
		AddressDetails details = addressSvc.findByAddrId(addressId);
		return new ResponseEntity<AddressDetails>(details, HttpStatus.OK);
	}

	public ResponseEntity<List<AddressDetails>> fetchAddressDetailsByCustId(int custId) throws Exception {
		List<AddressDetails> details = addressSvc.findByCustId(custId);
		return new ResponseEntity<List<AddressDetails>>(details, HttpStatus.OK);
	}
	
	private boolean isDataValid(AddressModel data) throws Exception {
		
		if(data.getAddressLine1()!=null && !data.getAddressLine1().equalsIgnoreCase("") && data.getAddressLine2()!=null 
				&& !data.getAddressLine2().equalsIgnoreCase("") && data.getCity()!=null && !data.getCity().equalsIgnoreCase("")
				&& data.getHouseNo()!=null && !data.getHouseNo().equalsIgnoreCase("") && data.getLandmark()!=null 
				&& !data.getLandmark().equalsIgnoreCase("") && data.getState()!=null && !data.getState().equalsIgnoreCase("")
				&& Long.compare(data.getPincode(),99999)>0 && Long.compare(data.getPincode(),1000000)<0) {
			return true;
		}
		
		return false;
	}
	
}
