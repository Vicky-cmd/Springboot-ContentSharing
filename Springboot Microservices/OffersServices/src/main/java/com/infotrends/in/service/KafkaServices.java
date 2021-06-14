package com.infotrends.in.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.infotrends.in.business.OffersOrc;

@Service
public class KafkaServices {

	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private OffersOrc offersOrc;
	
	public void sendMessage(String topicName,String msg) {
	   kafkaTemplate.send(topicName, msg);
	}   
	
	@KafkaListener(topics = "qujoosad-checkOffersAvailablityForCart", groupId = "qujoosad-consumers")
    public void checkOffersAvailablityForCart(String message) {
		try {
			JSONObject data = new JSONObject(message);
			System.out.println("Received Messasge in group - group-id: " + data.toString());
			offersOrc.checkForAvailableOffers(data);
		}catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@KafkaListener(topics = "qujoosad-removeOffersForCart", groupId = "qujoosad-consumers")
    public void removeOffersForCart(String message) {
		try {
			JSONObject data = new JSONObject(message);
			System.out.println("Received Messasge in group - group-id: " + data.toString());
			offersOrc.removeOffersForCart(data);
		}catch (Exception e) {
			e.printStackTrace();
		}
    }


	@KafkaListener(topics = "qujoosad-paymentCompletedInCart", groupId = "qujoosad-consumers")
    public void updateOfferDetails(String message) {
		try {
			JSONObject data = new JSONObject(message);
			System.out.println("Received Messasge in group - group-id: " + data.toString());
			offersOrc.updateOfferDetails(data);
		}catch (Exception e) {
			e.printStackTrace();
		}
    }	
	
	@KafkaListener(topics = "testing", groupId = "qujoosad-consumers")
    public void testlisten(String message) {
		try {
			System.out.println("Received Messasge in group - group-id: " + message);
		}catch (Exception e) {
			e.printStackTrace();
		}
    }
}
