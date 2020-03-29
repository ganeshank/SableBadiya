package com.sb.integration.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class UserVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private String fullName;
	private String email;
	private String mobileNumber;
	private String password;
	private String userName;
	private Integer accountSourceType;
	
	private BigDecimal walletAmount;
	
	private RoleVo userRole;
	
	private List<AddressVo> addressList;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public RoleVo getUserRole() {
		return userRole;
	}
	public void setUserRole(RoleVo userRole) {
		this.userRole = userRole;
	}
	public Integer getAccountSourceType() {
		return accountSourceType;
	}
	public void setAccountSourceType(Integer accountSourceType) {
		this.accountSourceType = accountSourceType;
	}
	public List<AddressVo> getAddressList() {
		return addressList;
	}
	public void setAddressList(List<AddressVo> addressList) {
		this.addressList = addressList;
	}
	public BigDecimal getWalletAmount() {
		return walletAmount;
	}
	public void setWalletAmount(BigDecimal walletAmount) {
		this.walletAmount = walletAmount;
	}
	@Override
	public String toString() {
		return "UserVo [userId=" + userId + ", fullName=" + fullName + ", email=" + email + ", mobileNumber="
				+ mobileNumber + ", password=" + password + ", userName=" + userName + ", userRole=" + userRole + "]";
	}
	
}
