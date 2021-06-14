package com.infotrends.in.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infotrends.in.business.OffersOrc;
import com.infotrends.in.data.OffersData;
import com.infotrends.in.model.ResponseModel;
import com.infotrends.in.service.OffersServices;

@RestController
@RequestMapping("/api/v1/offers")
public class OffersServicesController {

	
	@Autowired
	private OffersServices offersSvc;
	
	@Autowired
	private OffersOrc offersOrc;
	
	@PostMapping("/createOffer")
	public ResponseEntity<ResponseModel> addNewOffers(@RequestBody OffersData data) {
		ResponseModel resp = new ResponseModel();
		try {
			
			data.setStatus("A");
			data = offersSvc.save(data);
			resp.setStatusCode(HttpStatus.OK.value());
			resp.setOffersId(data.getOfferId());
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.OK);
			
		}catch (Exception e) {
			resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.setErrorMsg("Oops! Something happened.");
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	


	@GetMapping("/collectOffer/{offerId}")
	public ResponseEntity<ResponseModel> collectOffer(@PathVariable("offerId") int offerId,  HttpServletRequest request) {
		ResponseModel resp = new ResponseModel();
		try {
			int custId = 0;
			if(request.getParameter("custId")!=null && !request.getParameter("custId").toString().isEmpty()) {
				custId = Integer.decode(request.getParameter("custId").toString());
			}
			OffersData data = offersSvc.findById(offerId);
			if(data!=null && data.getOfferId()>0) {
				if(data.getOfferType().startsWith("BD")) {
					data.setCustId(custId);
					offersSvc.update(data);
					resp.setStatusCode(HttpStatus.OK.value());
					resp.setOffersId(data.getOfferId());
					return new ResponseEntity<ResponseModel>(resp, HttpStatus.OK);
				} else {
					resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
					resp.setErrorMsg("Requested Offer Cannot be mapped to the User!");
					return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
				}
			} else {
				resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
				resp.setErrorMsg("Order not Found");
				return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.setErrorMsg("Oops! Something happened.");
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping("/applyOffersToCart")
	public ResponseEntity<ResponseModel> applyOfferToCart(@RequestParam("cartId") int cartId) {
		ResponseModel resp = new ResponseModel();
		try {
			return offersOrc.checkOffersAvailabilityForCart(cartId, resp, "api");
		}catch (Exception e) {
			resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.setErrorMsg("Oops! Something happened.");
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
