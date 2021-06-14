package com.infotrends.in.data;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OffersData {

	@Id
	private String id;
	
	private int offerId;
	private String offersDesc;
	private String offerType;
	private double offerValue;
	private int custId;
	private int itemId;
	private String status;
	private int taggedCartId;
	
	public int getOfferId() {
		return offerId;
	}
	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}
	public String getOffersDesc() {
		return offersDesc;
	}
	public void setOffersDesc(String offersDesc) {
		this.offersDesc = offersDesc;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public double getOfferValue() {
		return offerValue;
	}
	public void setOfferValue(double offerValue) {
		this.offerValue = offerValue;
	}
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getTaggedCartId() {
		return taggedCartId;
	}
	public void setTaggedCartId(int taggedCartId) {
		this.taggedCartId = taggedCartId;
	}
	
}
