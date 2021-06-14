package com.infotrends.in.business;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.infotrends.in.Connections.ConnectionInterface;
import com.infotrends.in.config.AppConfig;
import com.infotrends.in.data.AddressDetails;
import com.infotrends.in.data.CartData;
import com.infotrends.in.data.ItemData;
import com.infotrends.in.data.PaymentData;
import com.infotrends.in.model.RequestModel;
import com.infotrends.in.model.ResponseModel;
import com.infotrends.in.service.AddressServices;
import com.infotrends.in.service.CartDataServices;
import com.infotrends.in.service.KafkaServices;

@Component
public class ProcessOrc {
	
	@Autowired
	private ConnectionInterface connInterface;

	@Autowired
	private CartDataServices cartSvc;

	@Autowired
	private KafkaServices kafkaSvc;
	
	@Autowired
	private AddressServices addressSvc;
	
	public HashMap<String, Object> getItemData(int getpId, int currItemQty) throws Exception {
		
		JSONObject reqJson = createReqObject(getpId, currItemQty);
		HashMap<String, Object> response = connInterface.executesForHttp(AppConfig.inventory_lookupUrl, reqJson, "POST");
		return response;
	}

	public HashMap<String, Object> applyOffersForCartInSync(int cartId) throws Exception {
		
		String offersUrl = AppConfig.apply_offersUrl + "?cartId=" + cartId;
		HashMap<String, Object> response = connInterface.executesForHttp(offersUrl, new JSONObject(), "POST");
		return response;
	}
	
	private JSONObject createReqObject(int getpId, int currItemQty) {
		JSONObject reqJson = new JSONObject();
		reqJson.put("productId", getpId);
		reqJson.put("quantity", currItemQty);
		return reqJson;
	}
	
	

//	@Transactional(rollbackOn = {Exception.class})
	public ResponseEntity<ResponseModel> addItemToCart(RequestModel reqModel, ResponseModel respModel, CartData cart) throws Exception {
		double totPrice = 0.0;
		int totQty = 0;
		int currItemQty = reqModel.getQuantity();
		ItemData data = null;
		List<ItemData> cartItems = cart.getCartItems();
		for(int i=0; i<cartItems.size(); i++) {
			ItemData item = cartItems.get(i);
			totPrice += item.getTotalAmount();
			totQty += item.getQuantity();
			if(reqModel.getpId() == item.getpId()) {
				currItemQty+=item.getQuantity();
				reqModel.setExistingItem(true);
				double totAmt = currItemQty * item.getPrice();
				cartItems.get(i).setTotalAmount(totAmt);
				cartItems.get(i).setQuantity(currItemQty);
			}
		};
		
		HashMap<String, Object> response = getItemData(reqModel.getpId(), currItemQty);
		if(isItemAvailable(response)) {
			data = processResponse(response);
		} else {
			respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
			respModel.setErrorMsg(response.containsKey("errorMsg")?response.get("errorMsg").toString():"Oops! Something Happened.");
			return new ResponseEntity<ResponseModel>(respModel, HttpStatus.EXPECTATION_FAILED);
		}
		if(!reqModel.isExistingItem()) {
			data.setItemId(cartItems.size() + 1);
			cartItems.add(data);
		}
		cart.setTotQuantity(cart.getTotQuantity() + reqModel.getQuantity());
		cart.setTotalAmount(cart.getTotalAmount() + (reqModel.getQuantity() * data.getPrice()));
		cart.setCartItems(cartItems);
		cartSvc.update(cart);
		if(reqModel.isApplyOffersInSync()) {
			HashMap<String, Object> offerResp = applyOffersForCartInSync(cart.getCartId());
			if(offerResp.get("statusCode")!=null && offerResp.get("statusCode").toString().equalsIgnoreCase("200")) {
				double discAmt = Double.parseDouble(offerResp.get("totalAmount").toString());
				String offerdesc = offerResp.get("offerDesc").toString();
				int offerId = Integer.decode(offerResp.get("offersId").toString());
				cart.setOfferId(offerId);
				cart.setOffersDesc(offerdesc);
				cart.setOffersApplied("Y");
				cart.setDiscAmt(discAmt);
				cartSvc.update(cart);
			} else if(offerResp.get("statusCode")!=null && offerResp.get("statusCode").toString().equalsIgnoreCase("500")) {
				checkForAvailableOffers(cart.getCartId());
			}
			
		} else {
			checkForAvailableOffers(cart.getCartId());
		}
		setResponseModel(respModel, cart);
		return new ResponseEntity<ResponseModel>(respModel,HttpStatus.CREATED);
	}

	@Transactional(rollbackOn = {Exception.class})
	public ResponseEntity<ResponseModel> createNewCartWithItem(int userId, RequestModel reqModel, ResponseModel respModel, CartData cart) throws Exception {
		int currItemQty = 0;
		currItemQty = reqModel.getQuantity();
		ItemData newItem = null;
		HashMap<String, Object> response = getItemData(reqModel.getpId(), currItemQty);
		if(isItemAvailable(response)) {
			newItem = processResponse(response);
			
			cart = cartSvc.createNewCartForUser(userId, newItem);
			checkForAvailableOffers(cart.getCartId());
			setResponseModel(respModel, cart);
			return new ResponseEntity<ResponseModel>(respModel, HttpStatus.CREATED);
		} else {
			respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
			respModel.setErrorMsg(response.containsKey("errorMsg")?response.get("errorMsg").toString():"Oops! Something Happened.");
			return new ResponseEntity<ResponseModel>(respModel, HttpStatus.EXPECTATION_FAILED);
		}
	}

	private void setResponseModel(ResponseModel respModel, CartData cart) {
		respModel.setCartId(cart.getCartId());
		respModel.setCartQuantity(cart.getTotQuantity());
		respModel.setTotalAmount(cart.getTotalAmount());
		respModel.setCartData(cart);
	}

	private ItemData processResponse(HashMap<String, Object> response) {
		ItemData data = new ItemData();
		double itemPrice = Double.valueOf(response.get("price").toString());
		int quantity = Integer.decode(response.get("quantity").toString());
		data.setpId(Integer.decode(response.get("pId").toString()));
		data.setName(response.get("name").toString());
		data.setDesc(response.get("desc").toString());
		data.setQuantity(quantity);
		data.setPrice(itemPrice);
		data.setTotalAmount(itemPrice * quantity);
		return data;
	}

	private boolean isItemAvailable(HashMap<String, Object> response) {
		if(response!=null) {
			if(response.containsKey("statusCode") && response.get("statusCode").toString().equals("200")) {
				return true;
			}
		}
		return false;
	}

	@Transactional(rollbackOn = {Exception.class})
	public ResponseEntity<ResponseModel> removeItemFromCart(RequestModel reqModel, ResponseModel respModel,
			CartData cart) throws Exception {
		int selectedItemId = reqModel.getpId();
		double totCartPrice = cart.getTotalAmount();
		boolean itemFoundInCart = false;
		List<ItemData> cartItemsLst = cart.getCartItems();
		if(cartItemsLst!=null) {			
			for(int i=0; i<cartItemsLst.size(); i++) {
				
				if(selectedItemId == cartItemsLst.get(i).getpId()) {
					ItemData selItem = cartItemsLst.get(i);
					
					if(reqModel.getQuantity()>selItem.getQuantity()) {
						respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
						respModel.setErrorMsg("Requested Quantity is greater than the existing Quantity in the cart");
						respModel.setStatusCode(selItem.getQuantity());
						return new ResponseEntity<ResponseModel>(respModel, HttpStatus.EXPECTATION_FAILED);
					} else if(reqModel.getQuantity()==selItem.getQuantity()) {
						totCartPrice -= selItem.getTotalAmount();
						cart.getCartItems().remove(i);
				    } else {
				    	totCartPrice -= (reqModel.getQuantity() * selItem.getPrice()); 
						int newQty = selItem.getQuantity() - reqModel.getQuantity();
						cart.getCartItems().get(i).setQuantity(newQty);
						cart.getCartItems().get(i).setTotalAmount(newQty * cartItemsLst.get(i).getPrice());
					}
					itemFoundInCart = true;
				}
			}
			if(!itemFoundInCart) {
				respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
				respModel.setErrorMsg("Requested item not found in the cart!");
				return new ResponseEntity<ResponseModel>(respModel, HttpStatus.EXPECTATION_FAILED);
			}
			cart.setTotQuantity(cart.getTotQuantity() - reqModel.getQuantity());
			cart.setTotalAmount(totCartPrice);
			cartSvc.update(cart);
			
			if(reqModel.isApplyOffersInSync()) {
				HashMap<String, Object> offerResp = applyOffersForCartInSync(cart.getCartId());
				if(offerResp.get("statusCode")!=null && offerResp.get("statusCode").toString().equalsIgnoreCase("200")) {
					double discAmt = Double.parseDouble(offerResp.get("totalAmount").toString());
					String offerdesc = offerResp.get("offerDesc").toString();
					int offerId = Integer.decode(offerResp.get("offersId").toString());
					cart.setOfferId(offerId);
					cart.setOffersDesc(offerdesc);
					cart.setOffersApplied("Y");
					cart.setDiscAmt(discAmt);
					cartSvc.update(cart);
				} else if(offerResp.get("statusCode")!=null && offerResp.get("statusCode").toString().equalsIgnoreCase("500")) {
					checkForAvailableOffers(cart.getCartId());
				}
				
			} else {
				checkForAvailableOffers(cart.getCartId());
			}
			
//			checkForAvailableOffers(cart.getCartId());
			setResponseModel(respModel, cart);
			respModel.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseModel>(respModel, HttpStatus.OK);
		} else {
			respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
			respModel.setErrorMsg("The Cart Does Not Have Any Items!");
			return new ResponseEntity<ResponseModel>(respModel, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public boolean isReqValid(int userId, RequestModel reqModel) {
		return ((userId>=0) && (reqModel.getpId()>0) && (reqModel.getQuantity()>0));
	}

	public ResponseEntity<ResponseModel> setResponseModelForFailure(ResponseModel respModel, int userId,
			RequestModel reqModel) {
		respModel.setStatusCode(HttpStatus.FAILED_DEPENDENCY.value());
		if(userId>=0) {
			respModel.setErrorMsg("Please Send A Proper User Id");
		} else if(reqModel.getpId()>0) {
			respModel.setErrorMsg("Please Send A Proper Item Id");
		} else if(reqModel.getQuantity()>0) {
			respModel.setErrorMsg("Quantity Cannot be Zero");
		}
		return new ResponseEntity<ResponseModel>(respModel, HttpStatus.FAILED_DEPENDENCY);
	}

	public void applyOffersToCart(JSONObject message) {
		// TODO Auto-generated method stub
		if(message.has("cartId")) {
			int cartId = message.getInt("cartId");
			double discAmt = message.getDouble("discountAmt");
			String offerdesc = message.getString("offerDesc");
			int offerId = message.getInt("offerId");
			CartData data = cartSvc.findCartById(cartId);
			data.setOfferId(offerId);
			data.setOffersDesc(offerdesc);
			data.setOffersApplied("Y");
			data.setDiscAmt(discAmt);
			cartSvc.update(data);
		}
	}
	
	public void checkForAvailableOffers(int cartId) {
		JSONObject reqObj = new JSONObject();
		reqObj.put("cartId", cartId);
		kafkaSvc.sendMessage("qujoosad-checkOffersAvailablityForCart", reqObj.toString());
	}

	public void untagOffersFromCart(int cartId) {
		JSONObject reqObj = new JSONObject();
		reqObj.put("cartId", cartId);
		kafkaSvc.sendMessage("qujoosad-checkOffersAvailablityForCart", reqObj.toString());
	}

	public void updatePaymentDetailsInCart(JSONObject jsonReq) {
		if(jsonReq.has("cartId") && jsonReq.has("userId")) {
			int cartId = jsonReq.getInt("cartId");
			int userId = jsonReq.getInt("userId");
			double amount = jsonReq.getDouble("amount");
			String receiptId = jsonReq.getString("receipt");
			String paymentId = jsonReq.getString("paymentId");
			CartData cart = cartSvc.findCartById(cartId);
			if(cart!=null && cart.getCartId()>0) {
				if(cart.getStatus().equals("A") && cart.getUserId() == userId) {
					PaymentData paymentInfo = new PaymentData();
					paymentInfo.setRazorPay_paymentAmount(amount);
					paymentInfo.setPaymentStatus("A");
					paymentInfo.setPaymentType("RP");
					paymentInfo.setPaymentMode("online");
					paymentInfo.setRazorPay_receiptId(receiptId);
					cart.setPaymentInfo(paymentInfo);
					cart.setStatus("C");
					cartSvc.update(cart);
					updateOfferDetails(cart);
				}
			}
		}
	}

	public void updateOfferDetails(CartData cartData) {
		JSONObject reqObj = new JSONObject();
		reqObj.put("cartId", cartData.getCartId());
		reqObj.put("offerId", cartData.getOfferId());
		reqObj.put("userId", cartData.getUserId());
		reqObj.put("opType", "removeItems");
		kafkaSvc.sendMessage("qujoosad-paymentCompletedInCart", reqObj.toString());
	}


	public ResponseEntity<ResponseModel> tagAddressToCustomer(int addrId, int custId) {
		ResponseModel resp = new ResponseModel();
		AddressDetails data = addressSvc.findByAddrId(addrId);
		if(data!=null && data.getAddressId()>0) {
			CartData cart = cartSvc.getActiveCartForuser(custId);
			
			cart.setDeliveryAddress(data);
			cartSvc.update(cart);
			resp.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.OK);
		} else {
			resp.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
			resp.setErrorMsg("Address Data Not Found!");
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<ResponseModel> markActiveCartForCoD(int custId) {
		ResponseModel resp = new ResponseModel();
		CartData cart = cartSvc.getActiveCartForuser(custId);
		if(cart!=null && cart.getCartId()>0) {

			PaymentData paymentDts = new PaymentData();
			paymentDts.setPaymentMode("CoD");
			paymentDts.setPaymentStatus("P");
			cart.setPaymentInfo(paymentDts);
			cart.setStatus("C");
			cartSvc.update(cart);
			updateOfferDetails(cart);
			resp.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.OK);
		} else {
			resp.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
			resp.setErrorMsg("Address Data Not Found!");
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.EXPECTATION_FAILED);
		}
	}
}
