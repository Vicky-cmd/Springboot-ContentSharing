package com.infotrends.in.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.infotrends.in.Connections.ConnectionInterface;
import com.infotrends.in.config.AppConfig;
import com.infotrends.in.dao.OffersRepository;
import com.infotrends.in.data.OffersData;
import com.infotrends.in.model.ResponseModel;
import com.infotrends.in.service.KafkaServices;
import com.infotrends.in.service.OffersServices;

@Component
public class OffersOrc {

	@Autowired
	private ConnectionInterface connInterface;

	@Autowired
	private OffersServices offersSvc;
	
	@Autowired
	private KafkaServices kafkaSvc;
	
	private double appliedOfferAmt = 0.0;
	private OffersData appliedOffer = null;
	private boolean offerApplied=false;
	
	public ResponseEntity<ResponseModel> checkOffersAvailabilityForCart(int cartId, ResponseModel resp, String reqType) throws Exception {
		
		appliedOfferAmt = 0.0;
		appliedOffer = null;
		offerApplied=false;
		HashMap<String, Object> respMap = getCartData(cartId);
		if(isCartAvailable(respMap)) {
			if(respMap.get("offersApplied")!=null && respMap.get("offersApplied").toString().equalsIgnoreCase("Y")) {
				int offerId = Integer.decode(respMap.get("offerId").toString());
				OffersData curOfferId = offersSvc.findById(offerId); 
				if(!curOfferId.getOfferType().startsWith("ID")) {
					resp.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
					resp.setErrorMsg("Offer has been already applied to the cart!");
					return new ResponseEntity<ResponseModel>(resp, HttpStatus.EXPECTATION_FAILED);
				}
			}
			int userId = Integer.decode(respMap.get("userId").toString());
			List<OffersData> custOffers = offersSvc.findActiveOffersByUserId(userId); 
			if(custOffers!=null && custOffers.size()>0) {
				custOffers.forEach(offer ->{
					double totalCartAmt = Double.valueOf(respMap.get("totalAmount").toString());
					double offerDisc = calculateOfferDiscount(totalCartAmt, offer, 1);
					if(appliedOffer==null || (Double.compare(offerDisc, appliedOfferAmt)>0)) {
						appliedOfferAmt = offerDisc;
						appliedOffer = offer;
						offerApplied = true;
					}
				});
			}
			if(!offerApplied) {
				List cartItems = (List) respMap.get("cartItems");
				if(cartItems!=null && cartItems.size()>0) {
					for(int i=0; i < cartItems.size(); i++) {
						double totalItemAmt = Double.valueOf(respMap.get("totalAmount").toString());
						Map<String, Object> item = (HashMap<String, Object>) cartItems.get(i);
						int itemId = Integer.decode(item.get("pId").toString());
						int ItemQty = Integer.decode(item.get("quantity").toString());
						List<OffersData> itemOffers = offersSvc.findActiveOffersByItemId(itemId);
						if(itemOffers!=null && itemOffers.size()>0) {
							itemOffers.forEach(offer ->{
								double offerDisc = calculateOfferDiscount(totalItemAmt, offer, ItemQty);
								if(appliedOffer==null || (Double.compare(offerDisc, appliedOfferAmt)>0)) {
									appliedOfferAmt = offerDisc;
									appliedOffer = offer;
									offerApplied = true;
								}
							});
						}
						
					}
				}
			}
			if(offerApplied) {
				if(reqType.equalsIgnoreCase("api")) {
					resp.setOffersId(appliedOffer.getOfferId());
					resp.setTotalAmount(appliedOfferAmt);
					resp.setOfferDesc(appliedOffer.getOffersDesc());
				} else {
					applyOffersToCart(appliedOffer, appliedOfferAmt, cartId);
				}
				if(!appliedOffer.getOfferType().startsWith("ID")) {
					appliedOffer.setStatus("P");
					appliedOffer.setTaggedCartId(cartId);
					offersSvc.update(appliedOffer);
				}
				resp.setStatusCode(HttpStatus.OK.value());
				return new ResponseEntity<ResponseModel>(resp, HttpStatus.OK);
			} else {
				resp.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
				resp.setErrorMsg("No Offers Found For the User/Cart");
				return new ResponseEntity<ResponseModel>(resp, HttpStatus.EXPECTATION_FAILED);
			}
			
		} else {
			resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
			resp.setErrorMsg("Cart Not Found/Active");
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
		}
		
	}

	private void applyOffersToCart(OffersData appliedOffer2, double appliedOfferAmt2, int cartId) {

		JSONObject jsonReq = new JSONObject();
		jsonReq.put("cartId", cartId);
		jsonReq.put("discountAmt", appliedOfferAmt2);
		jsonReq.put("offerDesc", appliedOffer2.getOffersDesc());
		jsonReq.put("offerId", appliedOffer2.getOfferId());
		kafkaSvc.sendMessage("qujoosad-applyOffersToCart", jsonReq.toString());
	}

	private double calculateOfferDiscount(double amount, OffersData offer, int quantity) {
		double offerDisc = 0.0;
		if(offer.getOfferType().equalsIgnoreCase("BDP") || offer.getOfferType().equalsIgnoreCase("IDP")) {
			offerDisc= amount * offer.getOfferValue()/100 * quantity;
		} else if(offer.getOfferType().equalsIgnoreCase("BDA") || offer.getOfferType().equalsIgnoreCase("IDA")) {
			offerDisc = offer.getOfferValue() * quantity;
		}
		return offerDisc;
	}

	public HashMap<String, Object> getCartData(int cartId) throws Exception {
		HashMap<String, Object> response = connInterface.executesForHttp(AppConfig.cart_lookupUrl + "/" + cartId, null, "GET");
		return response;
	}

	private boolean isCartAvailable(HashMap<String, Object> response) {
		if(response!=null) {
			if(response.containsKey("cartId") && !response.get("cartId").toString().isEmpty() 
					&& !response.get("cartId").toString().equals("0") && response.containsKey("status") 
					&& response.get("status").toString().equalsIgnoreCase("A")) {
				return true;
			}
		}
		return false;
	}

	public void checkForAvailableOffers(JSONObject message) {
		// TODO Auto-generated method stub
		try {
			int cartId = message.getInt("cartId");
			checkOffersAvailabilityForCart(cartId, new ResponseModel(), "kafka");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeOffersForCart(JSONObject data) {
		try {
			int cartId = data.getInt("cartId");
			HashMap<String, Object> respMap = getCartData(cartId);
			if(isCartAvailable(respMap)) {
				if(respMap.get("offersApplied")!=null && respMap.get("offersApplied").toString().equalsIgnoreCase("Y")) {
					int offerId = Integer.decode(respMap.get("offerId").toString());
					OffersData curOffer = offersSvc.findById(offerId);
					if(!curOffer.getOfferType().startsWith("ID")) {
						curOffer.setStatus("A");
						curOffer.setCustId(0);
						curOffer.setTaggedCartId(cartId);
						offersSvc.save(curOffer);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateOfferDetails(JSONObject data) {
		try {
			int cartId = data.getInt("cartId");
			HashMap<String, Object> respMap = getCartData(cartId);
			int offerId = Integer.decode(respMap.get("offerId").toString());
			OffersData curOffer = offersSvc.findById(offerId);
			if(curOffer!=null && !curOffer.getOfferType().startsWith("ID")) {
				curOffer.setStatus("C");
				curOffer.setTaggedCartId(cartId);
				offersSvc.update(curOffer);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
