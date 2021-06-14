package com.infotrends.in.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infotrends.in.business.AddressOrc;
import com.infotrends.in.data.AddressDetails;
import com.infotrends.in.data.CartData;
import com.infotrends.in.model.AddressModel;
import com.infotrends.in.model.RequestModel;
import com.infotrends.in.model.ResponseModel;

@RestController
@RequestMapping("/api/v1/address")
@CrossOrigin(origins = "*")
public class AddressController {

	
	@Autowired
	private AddressOrc addressOrc;
	
	@PostMapping("/addDeliveryAddress")
	public ResponseEntity<ResponseModel> addAddressDetails(@RequestBody AddressModel addrModel) {
			
		ResponseModel respModel = new ResponseModel();
		try {
			
			return addressOrc.saveDetails(addrModel);
		}catch (Exception e) {
			return new ResponseEntity<ResponseModel>(respModel,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}	

	
	@PostMapping("/updateDeliveryAddress")
	public ResponseEntity<ResponseModel> updateDeliveryAddress( @RequestParam("addrId") int addrId,@RequestBody AddressModel addrModel) {
			
		ResponseModel respModel = new ResponseModel();
		try {
			
			return addressOrc.updateDetails(addrId, addrModel);
		}catch (Exception e) {
			return new ResponseEntity<ResponseModel>(respModel,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}		

	
	@GetMapping("/findDeliveryAddress")
	public ResponseEntity<AddressDetails> findDeliveryAddress( @RequestParam("addrId") int addrId) {
			
		ResponseModel respModel = new ResponseModel();
		try {
			
			return addressOrc.fetchAddressDetails(addrId);
		}catch (Exception e) {
			return new ResponseEntity<AddressDetails>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}	

	
	@GetMapping("/findStoredAddr")
	public ResponseEntity<List<AddressDetails>> findStoredAddresses(@RequestParam("custId") int custId) {
			
		ResponseModel respModel = new ResponseModel();
		try {
			
			return addressOrc.fetchAddressDetailsByCustId(custId);
		}catch (Exception e) {
			return new ResponseEntity<List<AddressDetails>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}	
	

	@GetMapping("/deleteAddress")
	public ResponseEntity<ResponseModel> deleteAddress(@RequestParam("addrId") int addrId) {
			
		ResponseModel respModel = new ResponseModel();
		try {
			
			return addressOrc.deleteDetails(addrId);
		}catch (Exception e) {
			return new ResponseEntity<ResponseModel>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}	
}
