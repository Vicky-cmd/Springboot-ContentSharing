package com.infotrends.in.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.infotrends.in.model.ProductsModel;

@Document(collection = "Products")
public class ProductsData implements Serializable {

	@Id
	private String id;
	private int pId;
	private String name;
	private String desc;
	private String category;
	private String subcategory;
	private String brand;
	private int quantity;
	private double price;
	private String imgUrl;
	private Date timestamp = new Date();
	
	
	
	public ProductsData() {
		super();
	}


	public ProductsData(ProductsModel productModel) {
		name = productModel.getName();
		desc = productModel.getDesc();
		category = productModel.getCategory();
		price = productModel.getPrice();
		subcategory = productModel.getSubcategory();
		brand = productModel.getBrand();
		quantity = productModel.getQuantity();
		imgUrl = productModel.getImgUrl();
	}


	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPId() {
		return pId;
	}

	public void setPId(int id) {
		this.pId = id;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
