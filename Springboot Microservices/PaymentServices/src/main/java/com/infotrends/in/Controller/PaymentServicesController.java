package com.infotrends.in.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infotrends.in.business.PaymentOrc;
import com.infotrends.in.model.RequestModel;
import com.infotrends.in.model.ResponseModel;
import com.infotrends.in.service.PaymentDataServices;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "*")
public class PaymentServicesController {

	@Autowired
	private PaymentDataServices paymentSvc;
	
	@Autowired
	private PaymentOrc paymentOrc;
	
	@PostMapping(value = "/createOrder", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ResponseModel> createOrder(@RequestBody RequestModel reqModel) {
			
		ResponseModel respModel = new ResponseModel();
		try {
			if(Double.compare(reqModel.getAmount(), 1.00)>0) {
				return paymentOrc.createNewOrder(reqModel, respModel);
			} else {
				respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
				respModel.setErrorMsg("Amount must must be atleast greater than 1.00");
				return new ResponseEntity<ResponseModel>(respModel,HttpStatus.EXPECTATION_FAILED);
			}
		}catch (Exception e) {
			return new ResponseEntity<ResponseModel>(respModel,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/confirmPayment")
	public ResponseEntity<ResponseModel> comfirmPayment(@RequestBody RequestModel reqModel) {
			
		ResponseModel respModel = new ResponseModel();
		try {
//			if(Double.compare(reqModel.getAmount(), 1.00)>0) {
				return paymentOrc.confirmOrder(reqModel, respModel);
//			} else {
//				respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
//				respModel.setErrorMsg("Amount must must be atleast greater than 1.00");
//				return new ResponseEntity<ResponseModel>(respModel,HttpStatus.EXPECTATION_FAILED);
//			}
		}catch (Exception e) {
			return new ResponseEntity<ResponseModel>(respModel,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
		
	
}
