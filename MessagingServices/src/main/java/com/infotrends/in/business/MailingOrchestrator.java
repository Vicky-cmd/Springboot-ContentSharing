package com.infotrends.in.business;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infotrends.in.config.AppConstants;
import com.infotrends.in.dao.AuthenticationTokensRepository;
import com.infotrends.in.data.AuthenticationToken;

@Component
public class MailingOrchestrator {

	//{"isadmin":"Y","user_id":1,"fullname":"test","username":"test@gmail.com"}

	@Autowired
	AuthenticationTokensRepository authRepo;
	
	public void sendMailForUserRegistration(JSONObject jsonObj) {
		
		try {
			String token = generateAuthToken();
			int maxId = authRepo.findMaxAuth_id();
			token += maxId;
			
			int usrId = jsonObj.getInt("user_id");
			String username = jsonObj.getString("username");
			String fullname = jsonObj.getString("fullname");
			
			AuthenticationToken authToken = new AuthenticationToken();
			authToken.setToken(token);
			authToken.setUsr_id(usrId);
			authRepo.save(authToken);
			
			String encToken = Base64.getEncoder().encodeToString(token.getBytes());
			
			String validationUrl = AppConstants.emailValidationUrl + "/" + encToken;
			String mailBody = AppConstants.emailValidationMailContent;
			mailBody = mailBody.replace("<fullname>", fullname);
			mailBody = mailBody.replace("<url>", validationUrl);
			sendEmail(username, mailBody, "InfoTrends.in - Account Activation And Password Generation");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	private String generateAuthToken() {
		StringBuilder builder = new StringBuilder();
		String capChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerChar = "abcdefghijklmnopqrstuvwxyz";
        String numb = "0123456789";
        String range = capChar + lowerChar + numb;
        SecureRandom rand = new SecureRandom();
        for(int i=0; i<24; i++) {
        	builder.append(range.charAt(rand.nextInt(range.length())));
        }
        
        return builder.toString();
	}
	
	private void sendEmail(String toMail, String body, String subject) throws Exception {
		
		System.out.println("Sending Email to " + toMail);
		
		String smtpHostServer = "smtp.example.com";
	    final String fromEmail = "i*****@gmail.com";
	    final String password = "****";
	    Properties props = System.getProperties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
	    Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		
		Session session = Session.getInstance(props, auth);
		MimeMessage msg = new MimeMessage(session);
		msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		msg.addHeader("format", "flowed");
		msg.addHeader("Content-Transfer-Encoding", "8bit");
		
		msg.setFrom(new InternetAddress("no_reply@infoTrends.in", "NoReply-IN"));
		
		msg.setReplyTo(InternetAddress.parse("no_reply@infoTrends.in", false));
		
		msg.setSubject(subject, "UTF-8");
		
		msg.setContent(body, "text/html");
		
		msg.setSentDate(new Date());
		
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail, false));
		System.out.println("Message is ready");
		Transport.send(msg);  
		
	}

	
	public void expireAuthToken(JSONObject jsonObj) {
		try {
			if(jsonObj.has("enc-token")) {
				String encodedString = jsonObj.getString("enc-token");
				String token = new String(Base64.getDecoder().decode(encodedString));
				
				AuthenticationToken authtoken = authRepo.findByToken(token);
				if(authtoken!=null && authtoken.getToken()!=null) {
					authtoken.setStatus("I");
					authRepo.save(authtoken);
				}
				
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
