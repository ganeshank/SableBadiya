package com.sb.integration.vo;

import java.io.Serializable;

public class AddressVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long addressId;
	private String contactName;
	private String contactNumber;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private String pinCode;
	private String landMark;
	private String email;
	
	public Long getAddressId() {
	
		return addressId;
	}
	
	public void setAddressId(Long addressId) {
	
		this.addressId = addressId;
	}
	
	public String getContactName() {
	
		return contactName;
	}
	
	public void setContactName(String contactName) {
	
		this.contactName = contactName;
	}
	
	public String getContactNumber() {
	
		return contactNumber;
	}
	
	public void setContactNumber(String contactNumber) {
	
		this.contactNumber = contactNumber;
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
	
	public String getPinCode() {
	
		return pinCode;
	}
	
	public void setPinCode(String pinCode) {
	
		this.pinCode = pinCode;
	}
	
	public String getLandMark() {
	
		return landMark;
	}
	
	public void setLandMark(String landMark) {
	
		this.landMark = landMark;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {

		return "AddressVo [addressId=" + addressId + ", contactName=" + contactName + ", contactNumber=" + contactNumber
				+ ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", city=" + city + ", state="
				+ state + ", country=" + country + ", pinCode=" + pinCode + ", landMark=" + landMark + "]";
	}
	
	
}
