package com.cigital.integration.vo;

import java.io.Serializable;

public class QuantityVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long quantityId;
	private String weight;
	private String uom;
	
	public Long getQuantityId() {
	
		return quantityId;
	}
	
	public void setQuantityId(Long quantityId) {
	
		this.quantityId = quantityId;
	}
	
	public String getWeight() {
	
		return weight;
	}
	
	public void setWeight(String weight) {
	
		this.weight = weight;
	}
	
	public String getUom() {
	
		return uom;
	}
	
	public void setUom(String uom) {
	
		this.uom = uom;
	}

	@Override
	public String toString() {

		return "QuantityVo [quantityId=" + quantityId + ", weight=" + weight + ", uom=" + uom + "]";
	}
	
	
	
}
