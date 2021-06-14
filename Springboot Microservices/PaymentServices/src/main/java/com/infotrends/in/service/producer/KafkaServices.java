package com.infotrends.in.service.producer;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaServices {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	 
	public void sendMessage(String topicName,String msg) {
	   kafkaTemplate.send(topicName, msg);
	}   

}
