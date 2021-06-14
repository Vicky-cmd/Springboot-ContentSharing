package com.infotrends.in.business;

import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.infotrends.in.Connections.ConnectionInterface;
import com.infotrends.in.data.PaymentData;
import com.infotrends.in.model.RequestModel;
import com.infotrends.in.model.ResponseModel;
import com.infotrends.in.service.PaymentDataServices;
import com.infotrends.in.service.producer.KafkaServices;

@Component
public class PaymentOrc {

	@Autowired
	private ConnectionInterface connInterface;

	@Autowired
	private PaymentDataServices paymentDataServices;
	
	@Autowired
	private KafkaServices kafkaSvc;
	
	public ResponseEntity<ResponseModel> createNewOrder(RequestModel reqModel, ResponseModel respModel) throws Exception {
		double amount = reqModel.getAmount();
//		JSONObject jsonresp = new JSONObject();
		System.out.println(amount);
		
		PaymentData paymentData = new PaymentData();
		paymentData.setAmount(amount);
		paymentData.setCartId(reqModel.getCartId());
		paymentData.setUserId(reqModel.getUserId());
		paymentData.setStatus("");
		paymentData = paymentDataServices.save(paymentData);
		
		JSONObject jsonReq = new JSONObject();
		jsonReq.put("amount", amount*100);
		jsonReq.put("currency", "INR");
		jsonReq.put("receipt", paymentData.getReceiptId());
		
		HashMap<String, Object> resp = connInterface.executes("https://api.razorpay.com/v1/orders", jsonReq, "POST", "Y");
		System.out.println(resp);
		if(resp.get("status")!=null && resp.get("status").toString().equalsIgnoreCase("created")) {

			paymentData.setStatus("P");
			paymentData.setOrderId(resp.get("id").toString());
			paymentData.setEntity(resp.get("entity").toString());
			paymentDataServices.update(paymentData);
			
			respModel.setOrderId(resp.get("id").toString());
			respModel.setStatus(resp.get("status").toString());
			respModel.setAmount(amount);
			respModel.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseModel>(respModel, HttpStatus.CREATED);
		} else {

			paymentData.setStatus("F");
			paymentDataServices.update(paymentData);
			respModel.setStatus("Failure");
			respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
			respModel.setErrorMsg("Failed");
			return new ResponseEntity<ResponseModel>(respModel, HttpStatus.EXPECTATION_FAILED);
		}
		
	}
	
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	
    public static String calculateRFC2104HMAC(String data, String secret)
    throws Exception
    {
        String result;
        try {

            // get an hmac_sha256 key from the raw secret bytes
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256_ALGORITHM);

            // get an hmac_sha256 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

            // base64-encode the hmac
            result = DatatypeConverter.printHexBinary(rawHmac).toLowerCase();

        } catch (Exception e) {
            throw new Exception("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

	public ResponseEntity<ResponseModel> confirmOrder(RequestModel reqModel, ResponseModel respModel) throws Exception {
		
//		SecretKeySpec keySpec = new SecretKeySpec(
//		        (reqModel.getOrderId() + "|" +   "rzp_test_1OnHyQf62f3JNR").getBytes(),
//		        "HmacSHA1");
//
//		Mac mac = Mac.getInstance("HmacSHA1");
//		mac.init(keySpec);
//		byte[] result = mac.doFinal("foo".getBytes());

//		BASE64Encoder encoder = new BASE64Encoder();
		
		String generated_signature = calculateRFC2104HMAC(reqModel.getRazorpay_order_id() + "|" +  reqModel.getRazorpay_payment_id(), "iYXMef9vScvWtLemApt8sjRs");
		System.out.println(generated_signature);
		System.out.println(reqModel.getRazorpay_signature());
		if(generated_signature.equals(reqModel.getRazorpay_signature())) {
			
			
			PaymentData data = paymentDataServices.findByOrderId(reqModel.getRazorpay_order_id());
			if(data!=null && data.getAttemptId()>0) {
				if(data.getStatus().equalsIgnoreCase("A")) {
					respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
					respModel.setErrorMsg("Order is already Completed!");
					return new ResponseEntity<ResponseModel>(respModel, HttpStatus.EXPECTATION_FAILED);
				} 
				data.setStatus("A");
				data.setOrderId(reqModel.getRazorpay_order_id());
				data.setPaymentId(reqModel.getRazorpay_payment_id());
				data.setSignature(generated_signature);
				paymentDataServices.update(data);
				applyOffersToCart(data);
				respModel.setStatusCode(HttpStatus.OK.value());
				return new ResponseEntity<ResponseModel>(respModel, HttpStatus.OK);
			} else {
				respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
				respModel.setErrorMsg("No Order Found For the Order ID!");
				return new ResponseEntity<ResponseModel>(respModel, HttpStatus.EXPECTATION_FAILED);
			}
		} else {
			respModel.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
			respModel.setErrorMsg("Signature Does Not Match With the Generated Signature");
			return new ResponseEntity<ResponseModel>(respModel, HttpStatus.EXPECTATION_FAILED);
		}
	}

	private void applyOffersToCart(PaymentData data) {

		JSONObject jsonReq = new JSONObject();
		jsonReq.put("cartId", data.getCartId());
		jsonReq.put("userId", data.getUserId());
		jsonReq.put("amount", data.getAmount());
		jsonReq.put("receipt", data.getReceiptId());
		jsonReq.put("paymentId", data.getPaymentId());
		
		kafkaSvc.sendMessage("qujoosad-updatePaymentDetailsInCart", jsonReq.toString());
	}
	
}
