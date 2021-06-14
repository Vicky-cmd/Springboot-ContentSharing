package com.infotrends.in.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infotrends.in.business.ProcessOrc;
import com.infotrends.in.data.CartData;
import com.infotrends.in.data.ItemData;
import com.infotrends.in.model.RequestModel;
import com.infotrends.in.model.ResponseModel;
import com.infotrends.in.service.CartDataServices;
import com.infotrends.in.service.KafkaServices;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin(origins = "*")
public class CartController {

	@Autowired
	private ProcessOrc processOrc;

	@Autowired
	private CartDataServices cartSvc;
	
	@Autowired
	KafkaServices kafkaSvc;
	
	@PostMapping("/addItemToCart/{id}")
	public ResponseEntity<ResponseModel> addItemToCart(@PathVariable("id") int userId, @RequestBody RequestModel reqModel) {
			
		ResponseModel respModel = new ResponseModel();
		try {
			CartData cart = cartSvc.getActiveCartForuser(userId);
			if(cart==null || cart.getCartId()<=0) {
				return processOrc.createNewCartWithItem(userId, reqModel, respModel, cart);
			} else {
				return processOrc.addItemToCart(reqModel, respModel, cart);
			}
		}catch (Exception e) {
			return new ResponseEntity<ResponseModel>(respModel,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@PostMapping("/removeItemFromCart/{id}")
	public ResponseEntity<ResponseModel> removeItemFromCart(@PathVariable("id") int userId, @RequestBody RequestModel reqModel) {
			
		ResponseModel respModel = new ResponseModel();
		try {
			if(processOrc.isReqValid(userId, reqModel)) {
				CartData cart = cartSvc.getActiveCartForuser(userId);
				if(cart==null || cart.getCartId()<=0) {
					respModel.setStatusCode(HttpStatus.NOT_FOUND.value());
					respModel.setErrorMsg("No Active Cart Available for this user!");
					return new ResponseEntity<ResponseModel>(respModel, HttpStatus.NOT_FOUND);
				} else {
					return processOrc.removeItemFromCart(reqModel, respModel, cart);
				}
			} else {
				return processOrc.setResponseModelForFailure(respModel, userId, reqModel);
			}
		}catch (Exception e) {
			return new ResponseEntity<ResponseModel>(respModel,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	@GetMapping("/getCartDetails/{cart_id}")
	public ResponseEntity<CartData> getCartDetails(@PathVariable("cart_id") int cartId) {
		try {
			CartData cart = cartSvc.findCartById(cartId);
			if(cart!=null && cart.getCartId()>0) {
				return new ResponseEntity<CartData>(cart, HttpStatus.OK);
			} else {
				return new ResponseEntity<CartData>(cart, HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			return new ResponseEntity<CartData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@GetMapping("/getActiveCart/{userId}")
	public ResponseEntity<CartData> getActiveCart(@PathVariable("userId") int userId) {
		try {
			CartData cart = cartSvc.getActiveCartForuser(userId);
			if(cart!=null && cart.getCartId()>0) {
				return new ResponseEntity<CartData>(cart, HttpStatus.OK);
			} else {
				return new ResponseEntity<CartData>(cart, HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			return new ResponseEntity<CartData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/abandonCart/{cart_id}")
	public ResponseEntity<ResponseModel> abandonCart(@PathVariable("cart_id") int cartId) {
		ResponseModel resp = new ResponseModel();
		try {
			CartData cart = cartSvc.findCartById(cartId);
			if(cart!=null && cart.getCartId()>0) {
				if(cart.getStatus().equalsIgnoreCase("A")) {
					cart.setStatus("I");
					cartSvc.update(cart);
					
					processOrc.untagOffersFromCart(cartId);
					resp.setStatusCode(200);
					return new ResponseEntity<ResponseModel>(resp, HttpStatus.OK);
				} else {
					resp.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
					resp.setErrorMsg("Requested Cart is not active");
					return new ResponseEntity<ResponseModel>(resp, HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
				resp.setErrorMsg("Cart Not Found!");
				return new ResponseEntity<ResponseModel>(resp, HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.setErrorMsg("Oops! Something Happened.");
			return new ResponseEntity<ResponseModel>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/sendMsgToKafka/{msg}")
	public void sendMsgToKafka(@PathVariable("msg") String msg) {
		kafkaSvc.sendMessage("testing", msg);
	}

	@PutMapping("/tagAddressToCustomer")
	public ResponseEntity<ResponseModel> tagAddressToCustomer(@RequestParam("addrId") int addrId, @RequestParam("custId") int custId) {
		try {
			return processOrc.tagAddressToCustomer(addrId, custId);
		}catch (Exception e) {
			return new ResponseEntity<ResponseModel>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@PutMapping("/markActiveCartForCoD")
	public ResponseEntity<ResponseModel> markActiveCartForCoD(@RequestParam("custId") int custId) {
		try {
			return processOrc.markActiveCartForCoD(custId);
		}catch (Exception e) {
			return new ResponseEntity<ResponseModel>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getAllCarts")
	public ResponseEntity<List<CartData>> getAllCarts(@RequestParam("custId") int custId) {
		try {
			List<CartData> cartLst = cartSvc.findallCarts(custId);
			if(cartLst!=null && cartLst.size()>0) {
				return new ResponseEntity<List<CartData>>(cartLst, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<CartData>>(new ArrayList<CartData>(), HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			return new ResponseEntity<List<CartData>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
