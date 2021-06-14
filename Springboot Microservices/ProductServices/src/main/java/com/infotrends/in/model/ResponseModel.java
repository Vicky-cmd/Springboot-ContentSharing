package com.infotrends.in.model;

import com.infotrends.in.data.ProductsData;

public class ResponseModel {

	
	private int pId;
	private String name;
	private String desc;
	private String category;
	private String subcategory;
	private String brand;
	private double price;
	private int quantity;
	

	private int statusCode;
	private String errorMsg;
	
	public ResponseModel() {
	}

	public ResponseModel(ProductsData productsData) {
		this(productsData, productsData.getQuantity());
	}
	
	public ResponseModel(ProductsData productsData, int quantity) {
		pId = productsData.getPId();
		name = productsData.getName();
		desc = productsData.getDesc();
		category = productsData.getCategory();
		subcategory = productsData.getSubcategory();
		brand = productsData.getBrand();
		price = productsData.getPrice();
		this.quantity = quantity;
	}
	
	
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

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
	
	
}
