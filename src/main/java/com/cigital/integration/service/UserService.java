package com.cigital.integration.service;

import java.sql.Connection;
import java.util.List;

import com.cigital.integration.vo.AddressVo;
import com.cigital.integration.vo.RoleVo;
import com.cigital.integration.vo.SellerVo;
import com.cigital.integration.vo.UserVo;

public interface UserService {

	public void registerNewUser(UserVo userVo, Connection con) throws Exception;

	public Boolean isUserNameExist(String userName, Connection con) throws Exception;

	public UserVo checkLoginCredential(String emailOrConatct, String password, Connection con) throws Exception;

	public Boolean isEmailOrContactAlreadyExist(String email, String contact, Connection con) throws Exception;
	
	public List<AddressVo> getAddressesForUser(Connection con, Long userId)throws Exception;
	
	public void addAddressForUser(Connection con, AddressVo addressVo, Long userId, Boolean isGuestUser)throws Exception;
	
	public void createUserRole(Connection con, Long userId, Integer roleId)throws Exception;
	
	public RoleVo getRoleForUser(Connection con, Long userId)throws Exception;
	
	public void addSeller(Connection con, SellerVo sellerVo)throws Exception;
	
	public void updateAddress(Connection con, AddressVo addressVo)throws Exception;
	
	public void updateUserPersonalInfo(Connection con, UserVo userVo)throws Exception;
	
	public void saveCustomerQuery(Connection con, String name, String email, String contact, String subject, String message)throws Exception;
}
