package com.sb.integration.vo;

import java.io.Serializable;
import java.util.List;

public class CategoryVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long categoryId;
	private String categoryName;
	private String categoryDesc;
	private Long MediaId;
	private String mediaName;
	private String mediaWebpath;
	private Integer mediaTypeId;
	
	private List<GoodsVo> goods;
	private String categoryMediaForder;
	
	private List<CategoryVo> categoryVos;
	private Long parentCategoryId;
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public Long getMediaId() {
		return MediaId;
	}
	public void setMediaId(Long mediaId) {
		MediaId = mediaId;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public String getMediaWebpath() {
		return mediaWebpath;
	}
	public void setMediaWebpath(String mediaWebpath) {
		this.mediaWebpath = mediaWebpath;
	}
	public Integer getMediaTypeId() {
		return mediaTypeId;
	}
	public void setMediaTypeId(Integer mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}
	public List<GoodsVo> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodsVo> goods) {
		this.goods = goods;
	}
	public String getCategoryMediaForder() {
		return categoryMediaForder;
	}
	public void setCategoryMediaForder(String categoryMediaForder) {
		this.categoryMediaForder = categoryMediaForder;
	}
	public List<CategoryVo> getCategoryVos() {
		return categoryVos;
	}
	public Long getParentCategoryId() {
		return parentCategoryId;
	}
	public void setCategoryVos(List<CategoryVo> categoryVos) {
		this.categoryVos = categoryVos;
	}
	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}
	@Override
	public String toString() {
		return "CategoryVo [categoryId=" + categoryId + ", categoryName=" + categoryName + ", categoryDesc="
				+ categoryDesc + ", MediaId=" + MediaId + ", mediaName=" + mediaName + ", mediaWebpath=" + mediaWebpath
				+ ", mediaTypeId=" + mediaTypeId + ", goods=" + goods + ", categoryMediaForder=" + categoryMediaForder
				+ ", categoryVos=" + categoryVos + ", parentCategoryId=" + parentCategoryId + "]";
	}
	
	
	
}
