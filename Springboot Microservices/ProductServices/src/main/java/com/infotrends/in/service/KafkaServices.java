package com.infotrends.in.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.infotrends.in.business.ProductsOrc;

@Service
public class KafkaServices {

	@Autowired
	private ProductsOrc productsOrc;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	 
	public void sendMessage(String topicName,String msg) {
	   kafkaTemplate.send(topicName, msg);
	}   
	
	@KafkaListener(topics = "qujoosad-paymentCompletedInCart", groupId = "qujoosad-cartConsumers")
    public void modifyProductAvailability(String message) {
		try {
			JSONObject data = new JSONObject(message);
			System.out.println("Received Messasge in group - group-id: " + data.toString());
			productsOrc.updateProductInventoryData(data);
		} catch(Exception e) { 	
			e.printStackTrace();
		}
    }
}
