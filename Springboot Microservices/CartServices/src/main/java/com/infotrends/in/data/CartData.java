package com.infotrends.in.data;

import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class CartData {

	
	@Id
	private String _id;
	
	private int cartId;
	private String status = "A";
	private double totalAmount = 0.0;
	private double discAmt = 0.0;
	private int userId;
	private int offerId;
	private String offersDesc;
	private int totQuantity;
	private String offersApplied = "N";

	private List<ItemData> cartItems;
	private AddressDetails deliveryAddress;
	private PaymentData paymentInfo;
	
	private Date timestamp;

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double total_amount) {
		this.totalAmount = total_amount;
	}

	public double getDiscAmt() {
		return discAmt;
	}

	public void setDiscAmt(double discAmt) {
		this.discAmt = discAmt;
	}

	public List<ItemData> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<ItemData> cartItems) {
		this.cartItems = cartItems;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTotQuantity() {
		return totQuantity;
	}

	public void setTotQuantity(int totQuantity) {
		this.totQuantity = totQuantity;
	}

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

	public String getOffersApplied() {
		return offersApplied;
	}

	public void setOffersApplied(String offersApplied) {
		this.offersApplied = offersApplied;
	}

	public PaymentData getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(PaymentData paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public AddressDetails getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(AddressDetails deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

}
