package com.infotrends.in.Controller;

import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotrends.in.business.MailingOrchestrator;
import com.infotrends.in.dao.AuthenticationTokensRepository;
import com.infotrends.in.data.AuthenticationToken;

@RestController
@RequestMapping("/api/v1")
public class MessagingServicesController {


	@Autowired
	AuthenticationTokensRepository authRepo;

	@Autowired
	MailingOrchestrator mailOrc;
	
	
//	@PostMapping("/validateAuthToken")
	@PostMapping(path = "validateAuthToken" ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public String sendEmailToNewUser(@RequestBody String inp) {
		JSONObject resp = new JSONObject();
		JSONObject requestJSON = new JSONObject(inp);
		try {
			
			
			if(requestJSON.has("enc-token")) {
				String encodedString = requestJSON.getString("enc-token");
				String token = new String(Base64.getDecoder().decode(encodedString));
				
				AuthenticationToken authtoken = authRepo.findByToken(token);
				if(authtoken!=null && authtoken.getToken()!=null) {
					long diffInMillies = Math.abs(authtoken.getTimesamp().getTime() - (new Date()).getTime());
					long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

					if(authtoken.getStatus()!=null && authtoken.getStatus().equalsIgnoreCase("A") && diff<1) {
						ObjectMapper mapper = new ObjectMapper();
						String repStr = mapper.writeValueAsString(authtoken);
						resp = new JSONObject(repStr);
						resp.put("statusCode", "200");
					} else {
						mailOrc.expireAuthToken(requestJSON);
						resp.put("statusCode", "203");
						resp.put("errorMsg", "Token Not Foundas Expired!");
					}
				} else {
					resp.put("statusCode", "201");
					resp.put("errorMsg", "Token Not Found!");
				}
			} else {
				resp.put("statusCode", "202");
				resp.put("errorMsg", "Please Send the Token For validation in the request.");
			}
			
		} catch (Exception e) {
			resp.put("statusCode", "500");
			resp.put("errorMsg", "Something Happend!");
		}
		
		return resp.toString(); 
	}
}
