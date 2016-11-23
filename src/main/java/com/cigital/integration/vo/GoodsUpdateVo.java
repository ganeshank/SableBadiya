package com.cigital.integration.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class GoodsUpdateVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long goodsId;
	private BigDecimal price;
	private BigDecimal msrp;
	private Boolean inStock;
	private Boolean isTodaysDeal;
	
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getMsrp() {
		return msrp;
	}
	public void setMsrp(BigDecimal msrp) {
		this.msrp = msrp;
	}
	public Boolean getInStock() {
		return inStock;
	}
	public void setInStock(Boolean inStock) {
		this.inStock = inStock;
	}
	public Boolean getIsTodaysDeal() {
		return isTodaysDeal;
	}
	public void setIsTodaysDeal(Boolean isTodaysDeal) {
		this.isTodaysDeal = isTodaysDeal;
	}
	@Override
	public String toString() {
		return "GoodsUpdateVo [goodsId=" + goodsId + ", price=" + price + ", msrp=" + msrp + ", inStock=" + inStock
				+ "]";
	}
	
}
