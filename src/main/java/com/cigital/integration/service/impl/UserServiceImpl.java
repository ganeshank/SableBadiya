package com.cigital.integration.service.impl;

import java.sql.Connection;
import java.util.List;

import com.cigital.integration.dao.UserRepository;
import com.cigital.integration.dao.impl.UserRepositoryImpl;
import com.cigital.integration.service.UserService;
import com.cigital.integration.vo.AddressVo;
import com.cigital.integration.vo.RoleVo;
import com.cigital.integration.vo.SellerVo;
import com.cigital.integration.vo.UserVo;

public class UserServiceImpl implements UserService {
	
	public UserRepository userRepository = new UserRepositoryImpl();

	public void registerNewUser(UserVo userVo, Connection con)throws Exception {
	
		try {
			userRepository.registerNewUser(userVo, con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public Boolean isUserNameExist(String userName, Connection con) throws Exception {

		try {
			Boolean isUserExist = userRepository.isUserNameExist(userName, con);
			return !isUserExist;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public UserVo checkLoginCredential(String emailOrConatct, String password, Connection con)throws Exception {
		try {
			return userRepository.checkLoginCredential(emailOrConatct, password, con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
	}

	public Boolean isEmailOrContactAlreadyExist(String email, String contact, Connection con) throws Exception {

		try {
			return userRepository.isEmailOrContactAlreadyExist(email, contact, con);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public List<AddressVo> getAddressesForUser(Connection con, Long userId) throws Exception {
	
		try {
			return userRepository.getAddressesForUser(con, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public void addAddressForUser(Connection con, AddressVo addressVo, Long userId, Boolean isGuestUser) throws Exception {
		try {
			userRepository.addAddressForUser(con, addressVo, userId, isGuestUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public void createUserRole(Connection con, Long userId, Integer roleId)throws Exception {
		try {
			userRepository.createUserRole(con, userId, roleId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public RoleVo getRoleForUser(Connection con, Long userId) throws Exception {
		try {
			return userRepository.getRoleForUser(con, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		
	}

	@Override
	public void addSeller(Connection con, SellerVo sellerVo) throws Exception {

		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAddress(Connection con, AddressVo addressVo) throws Exception {
		try {
			userRepository.updateAddress(con, addressVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateUserPersonalInfo(Connection con, UserVo userVo) throws Exception {
		try {
			userRepository.updateUserPersonalInfo(con, userVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveCustomerQuery(Connection con, String name, String email, String contact, String subject,
			String message) throws Exception {
		try {
			userRepository.saveCustomerQuery(con, name, email, contact, subject, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
