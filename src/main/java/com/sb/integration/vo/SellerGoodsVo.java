package com.sb.integration.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class SellerGoodsVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long sellerGoodsId;
	private Long sellerId;
	private Long goodsId;
	private BigDecimal price;
	private BigDecimal msrp;
	private Boolean inStock;
	
	public Long getSellerGoodsId() {
		return sellerGoodsId;
	}
	public void setSellerGoodsId(Long sellerGoodsId) {
		this.sellerGoodsId = sellerGoodsId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
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
	
}
