package com.infotrends.in.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.infotrends.in.business.ProcessOrc;

@Service
public class KafkaServices {

	@Autowired
	private ProcessOrc processOrc;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	 
	public void sendMessage(String topicName,String msg) {
	   kafkaTemplate.send(topicName, msg);
	}   
	
	@KafkaListener(topics = "qujoosad-applyOffersToCart", groupId = "qujoosad-consumers")
    public void applyOffersToCart(String message) {
		try {
			JSONObject data = new JSONObject(message);
			System.out.println("Received Messasge in group - group-id: " + data.toString());
			processOrc.applyOffersToCart(data);
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
	

	@KafkaListener(topics = "qujoosad-updatePaymentDetailsInCart", groupId = "qujoosad-consumers")
    public void updatePaymentDetailsInCart(String message) {
		try {
			JSONObject data = new JSONObject(message);
			System.out.println("Received Messasge in group - group-id: " + data.toString());
			processOrc.updatePaymentDetailsInCart(data);
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}
