package com.cigital.integration.vo;

import java.io.Serializable;

public class RoleVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long roleId;
	private String roleName;
	private String roleDesc;
	
	public Long getRoleId() {
	
		return roleId;
	}
	
	public void setRoleId(Long roleId) {
	
		this.roleId = roleId;
	}
	
	public String getRoleName() {
	
		return roleName;
	}
	
	public void setRoleName(String roleName) {
	
		this.roleName = roleName;
	}
	
	public String getRoleDesc() {
	
		return roleDesc;
	}
	
	public void setRoleDesc(String roleDesc) {
	
		this.roleDesc = roleDesc;
	}
	
	

}
