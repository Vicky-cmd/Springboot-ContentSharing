package com.infotrends.in.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "AuthenticationTokens")
public class AuthenticationToken {

	@Id
	@GeneratedValue
	private int auth_id;
	
	@NotNull
	@Column(name = "token")
	private String token;
	
	@NotNull
	@Column(name = "user_id")
	private int usr_id;
	
	@Column(name = "status")
	private String status = "A";
	
	@Column(name= "timestamp")
	private Date timesamp = new Date();

	public AuthenticationToken() {
	}

	public int getAuth_id() {
		return auth_id;
	}

	public void setAuth_id(int auth_id) {
		this.auth_id = auth_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUsr_id() {
		return usr_id;
	}

	public void setUsr_id(int usr_id) {
		this.usr_id = usr_id;
	}

	public Date getTimesamp() {
		return timesamp;
	}

	public void setTimesamp(Date timesamp) {
		this.timesamp = timesamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
