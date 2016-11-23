package com.cigital.integration.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class CartItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long cartItemId;
	private Long cartId;
	private String quantity;
	private String uom;
	private BigDecimal price;
	private BigDecimal msrp;
	private Long goodsId;
	private BigDecimal saving;
	
	private GoodsVo goodsVo;
	private List<QuantityVo> quantityVos;
	private SellerVo sellerVo;
	private Boolean isCancelled;
	
	public Long getCartItemId() {
	
		return cartItemId;
	}
	
	public void setCartItemId(Long cartItemId) {
	
		this.cartItemId = cartItemId;
	}
	
	public Long getCartId() {
	
		return cartId;
	}
	
	public void setCartId(Long cartId) {
	
		this.cartId = cartId;
	}
	
	public String getQuantity() {
	
		return quantity;
	}
	
	public void setQuantity(String quantity) {
	
		this.quantity = quantity;
	}
	
	public String getUom() {
	
		return uom;
	}
	
	public void setUom(String uom) {
	
		this.uom = uom;
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

	public Long getGoodsId() {
	
		return goodsId;
	}
	
	public void setGoodsId(Long goodsId) {
	
		this.goodsId = goodsId;
	}

	
	
	public BigDecimal getSaving() {
	
		return saving;
	}

	
	public void setSaving(BigDecimal saving) {
	
		this.saving = saving;
	}

	public GoodsVo getGoodsVo() {
	
		return goodsVo;
	}

	
	public void setGoodsVo(GoodsVo goodsVo) {
	
		this.goodsVo = goodsVo;
	}

	
	public List<QuantityVo> getQuantityVos() {
	
		return quantityVos;
	}

	
	public void setQuantityVos(List<QuantityVo> quantityVos) {
	
		this.quantityVos = quantityVos;
	}

	public SellerVo getSellerVo() {
		return sellerVo;
	}

	public void setSellerVo(SellerVo sellerVo) {
		this.sellerVo = sellerVo;
	}

	public Boolean getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	@Override
	public String toString() {

		return "CartItem [cartItemId=" + cartItemId + ", cartId=" + cartId + ", quantity=" + quantity + ", uom=" + uom
				+ ", price=" + price + ", msrp=" + msrp + ", goodsId=" + goodsId + "]";
	}

}
