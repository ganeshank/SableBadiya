package com.sb.integration.vo;

import java.io.Serializable;

public class SearchRequestVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String searchBy;
	private String searchValue;
	private String toDate;
	private String fromDate;
	public String getSearchBy() {
		return searchBy;
	}
	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
	

}
