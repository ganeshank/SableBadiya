package com.cigital.integration.vo;

import java.io.Serializable;

public class DeliveryTypeVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long deliveryTypeId;
	private String deliveryName;
	private String deliveryDesc;
	
	public Long getDeliveryTypeId() {
	
		return deliveryTypeId;
	}
	
	public void setDeliveryTypeId(Long deliveryTypeId) {
	
		this.deliveryTypeId = deliveryTypeId;
	}
	
	public String getDeliveryName() {
	
		return deliveryName;
	}
	
	public void setDeliveryName(String deliveryName) {
	
		this.deliveryName = deliveryName;
	}
	
	public String getDeliveryDesc() {
	
		return deliveryDesc;
	}
	
	public void setDeliveryDesc(String deliveryDesc) {
	
		this.deliveryDesc = deliveryDesc;
	}
	
	
}
