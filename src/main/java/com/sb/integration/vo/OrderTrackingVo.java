package com.sb.integration.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class OrderTrackingVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderNumber;
	private String orderStatus;
	private Timestamp trackDate;
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Timestamp getTrackDate() {
		return trackDate;
	}
	public void setTrackDate(Timestamp trackDate) {
		this.trackDate = trackDate;
	}
	
	
	
}
