package com.sb.integration.vo;

import java.io.Serializable;

public class CouponVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long couponId;
	private String couponNumber;
	private Integer offerPercentage;
	
	public Long getCouponId() {
		return couponId;
	}
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	public String getCouponNumber() {
		return couponNumber;
	}
	public void setCouponNumber(String couponNumber) {
		this.couponNumber = couponNumber;
	}
	public Integer getOfferPercentage() {
		return offerPercentage;
	}
	public void setOfferPercentage(Integer offerPercentage) {
		this.offerPercentage = offerPercentage;
	}
	
}
