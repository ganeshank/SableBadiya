package com.cigital.integration.vo;

import java.io.Serializable;

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
	
	private RoleVo userRole;
	
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
	@Override
	public String toString() {
		return "UserVo [userId=" + userId + ", fullName=" + fullName + ", email=" + email + ", mobileNumber="
				+ mobileNumber + ", password=" + password + ", userName=" + userName + "]";
	}
	
}
