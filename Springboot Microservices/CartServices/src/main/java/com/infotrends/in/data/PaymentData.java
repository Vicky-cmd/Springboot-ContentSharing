package com.infotrends.in.data;

public class PaymentData {

	
	private int paymentId;
	private String paymentType;
	private String paymentStatus;
	
	private String paymentMode;
	private String currencyType;
	private double amountTendered;
	private double amountReturned;
	
	private String razorPay_orderId;
	private String razorPay_receiptId;
	private double razorPay_paymentAmount;

	
	public int getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public double getAmountTendered() {
		return amountTendered;
	}
	public void setAmountTendered(double amountTendered) {
		this.amountTendered = amountTendered;
	}
	public double getAmountReturned() {
		return amountReturned;
	}
	public void setAmountReturned(double amountReturned) {
		this.amountReturned = amountReturned;
	}
	public String getRazorPay_orderId() {
		return razorPay_orderId;
	}
	public void setRazorPay_orderId(String razorPay_orderId) {
		this.razorPay_orderId = razorPay_orderId;
	}
	public String getRazorPay_receiptId() {
		return razorPay_receiptId;
	}
	public void setRazorPay_receiptId(String razorPay_receiptId) {
		this.razorPay_receiptId = razorPay_receiptId;
	}
	public double getRazorPay_paymentAmount() {
		return razorPay_paymentAmount;
	}
	public void setRazorPay_paymentAmount(double razorPay_paymentAmount) {
		this.razorPay_paymentAmount = razorPay_paymentAmount;
	}
	
}	
