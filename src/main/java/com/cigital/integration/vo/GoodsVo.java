package com.cigital.integration.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class GoodsVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long goodsId;
	private String goodsName;
	private String description;
	private Long categoryId;
	private Long mediaId;
	private String mediaName;
	private String webpath;

	private BigDecimal price;
	private BigDecimal msrp;
	private BigDecimal saving;
	private String quantity_per_annum;
	private String uom;
	private Boolean inStock;
	private Boolean inRupee;
	private Boolean isTodaysDeal;

	private List<QuantityVo> quantityVos;

	public Long getGoodsId() {

		return goodsId;
	}

	public void setGoodsId(Long goodsId) {

		this.goodsId = goodsId;
	}

	public String getGoodsName() {

		return goodsName;
	}

	public void setGoodsName(String goodsName) {

		this.goodsName = goodsName;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public Long getCategoryId() {

		return categoryId;
	}

	public void setCategoryId(Long categoryId) {

		this.categoryId = categoryId;
	}

	public Long getMediaId() {

		return mediaId;
	}

	public void setMediaId(Long mediaId) {

		this.mediaId = mediaId;
	}

	public String getMediaName() {

		return mediaName;
	}

	public void setMediaName(String mediaName) {

		this.mediaName = mediaName;
	}

	public String getWebpath() {

		return webpath;
	}

	public void setWebpath(String webpath) {

		this.webpath = webpath;
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

	
	
	public BigDecimal getSaving() {
	
		return saving;
	}

	
	public void setSaving(BigDecimal saving) {
	
		this.saving = saving;
	}

	public String getQuantity_per_annum() {
	
		return quantity_per_annum;
	}

	
	public void setQuantity_per_annum(String quantity_per_annum) {
	
		this.quantity_per_annum = quantity_per_annum;
	}

	public String getUom() {

		return uom;
	}

	public void setUom(String uom) {

		this.uom = uom;
	}

	public List<QuantityVo> getQuantityVos() {

		return quantityVos;
	}

	public void setQuantityVos(List<QuantityVo> quantityVos) {

		this.quantityVos = quantityVos;
	}

	public Boolean getInStock() {
		return inStock;
	}

	public void setInStock(Boolean inStock) {
		this.inStock = inStock;
	}

	public Boolean getInRupee() {
		return inRupee;
	}

	public void setInRupee(Boolean inRupee) {
		this.inRupee = inRupee;
	}

	public Boolean getIsTodaysDeal() {
		return isTodaysDeal;
	}

	public void setIsTodaysDeal(Boolean isTodaysDeal) {
		this.isTodaysDeal = isTodaysDeal;
	}

	@Override
	public String toString() {

		return "GoodsVo [goodsId=" + goodsId + ", goodsName=" + goodsName + ", description=" + description
				+ ", categoryId=" + categoryId + ", mediaId=" + mediaId + ", mediaName=" + mediaName + ", webpath="
				+ webpath + ", price=" + price + ", msrp=" + msrp + ", quantity_per_annum=" + quantity_per_annum
				+ ", uom=" + uom + ", quantityVos=" + quantityVos + "]";
	}

}
