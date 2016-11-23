package com.cigital.integration.vo;


public class SellerVo {
	private Integer sellerId;
	private String sellerName;
	private String sellerContact;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private Long userId;
	
	// These fields is used when admin assign seller for an line item.
	private Integer cartItemStatus;
	private String rejectReason;
	
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerContact() {
		return sellerContact;
	}
	public void setSellerContact(String sellerContact) {
		this.sellerContact = sellerContact;
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getCartItemStatus() {
		return cartItemStatus;
	}
	public void setCartItemStatus(Integer cartItemStatus) {
		this.cartItemStatus = cartItemStatus;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	
}
