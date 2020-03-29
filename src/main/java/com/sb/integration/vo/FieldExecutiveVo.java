package com.sb.integration.vo;

import java.io.Serializable;

public class FieldExecutiveVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer fieldExecutiveId;
	private String feName;
	private String feContact;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private Boolean active;
	private Long userId;
	public Integer getFieldExecutiveId() {
		return fieldExecutiveId;
	}
	public void setFieldExecutiveId(Integer fieldExecutiveId) {
		this.fieldExecutiveId = fieldExecutiveId;
	}
	public String getFeName() {
		return feName;
	}
	public void setFeName(String feName) {
		this.feName = feName;
	}
	public String getFeContact() {
		return feContact;
	}
	public void setFeContact(String feContact) {
		this.feContact = feContact;
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
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
