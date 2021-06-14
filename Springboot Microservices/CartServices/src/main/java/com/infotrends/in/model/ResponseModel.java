package com.infotrends.in.model;

import com.infotrends.in.data.CartData;

public class ResponseModel {
	
	private int statusCode;
	private String errorMsg;
	private int cartQuantity;
	private int cartId;
	private double totalAmount;
	private int addressId;
	private CartData cartData;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public int getCartQuantity() {
		return cartQuantity;
	}
	public void setCartQuantity(int cartQuantity) {
		this.cartQuantity = cartQuantity;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public CartData getCartData() {
		return cartData;
	}
	public void setCartData(CartData cartData) {
		this.cartData = cartData;
	}
	
}
