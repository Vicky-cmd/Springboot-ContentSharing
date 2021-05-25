package com.infotrends.in.service;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotrends.in.business.MailingOrchestrator;

@Service
public class MQConsumer {

	@Autowired
	MailingOrchestrator mailOrc;
	

	@RabbitListener(queues = "mailNewUser")
	public void processNewUserMailingReq(JSONObject jsonObject) {
		System.out.println(MQConsumer.class + " ------------------------------------------------------------------>");
		System.out.println(jsonObject.toString());
		mailOrc.sendMailForUserRegistration(jsonObject);
	}
	
	@RabbitListener(queues = "expireTokens")
	public void expireTokens(JSONObject jsonObject) {
		System.out.println(MQConsumer.class + " ------------------------------------------------------------------>");
		System.out.println(jsonObject.toString());
		mailOrc.expireAuthToken(jsonObject);
	}
	
}
