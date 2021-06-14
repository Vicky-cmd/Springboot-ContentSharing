package com.infotrends.in.data;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.infotrends.in.model.AddressModel;

@Document
public class AddressDetails {

	@Id
	private String id;
	
	private int addressId;
	private int custId;
	private String houseNo;
	private String addressLine1;
	private String addressLine2;
	private String landmark;
	private String city;
	private String state;
	private long pincode;
	private Date timestamp= new Date();
	

	public AddressDetails() {
	}

	public AddressDetails (AddressModel details) {
		super();
		this.addressId = details.getAddressId();
		this.custId = details.getCustId();
		this.houseNo = details.getHouseNo();
		this.addressLine1 = details.getAddressLine1();
		this.addressLine2 = details.getAddressLine2();
		this.landmark = details.getLandmark();
		this.city = details.getCity();
		this.state = details.getState();
		this.pincode = details.getPincode();
	}
	
	public void setAddressDetails(AddressModel details) {
		this.addressId = details.getAddressId();
		this.custId = details.getCustId();
		this.houseNo = details.getHouseNo();
		this.addressLine1 = details.getAddressLine1();
		this.addressLine2 = details.getAddressLine2();
		this.landmark = details.getLandmark();
		this.city = details.getCity();
		this.state = details.getState();
		this.pincode = details.getPincode();
	}
	
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getPincode() {
		return pincode;
	}
	public void setPincode(long pincode) {
		this.pincode = pincode;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}