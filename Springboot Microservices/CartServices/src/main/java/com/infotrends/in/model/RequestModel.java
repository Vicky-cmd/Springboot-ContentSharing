package com.infotrends.in.model;

public class RequestModel {
	private int pId;
	private int quantity;
	private boolean isExistingItem;
	private boolean applyOffersInSync = false;
	
	
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public boolean isExistingItem() {
		return isExistingItem;
	}
	public void setExistingItem(boolean isExistingItem) {
		this.isExistingItem = isExistingItem;
	}
	public boolean isApplyOffersInSync() {
		return applyOffersInSync;
	}
	public void setApplyOffersInSync(boolean applyOffersInSync) {
		this.applyOffersInSync = applyOffersInSync;
	}
	
}
