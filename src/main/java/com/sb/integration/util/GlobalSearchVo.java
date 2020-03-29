package com.sb.integration.util;

import java.io.Serializable;

public class GlobalSearchVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String value;
	private String unitPrice;
	private String qtyPerUom;
	private String uom;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getQtyPerUom() {
		return qtyPerUom;
	}
	public String getUom() {
		return uom;
	}
	public void setQtyPerUom(String qtyPerUom) {
		this.qtyPerUom = qtyPerUom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	@Override
	public String toString() {
		return "GlobalSearchVo [key=" + key + ", value=" + value + ", unitPrice=" + unitPrice + ", qtyPerUom="
				+ qtyPerUom + ", uom=" + uom + "]";
	}
	
	
}
